package kr.co.ssalon.domain.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.co.ssalon.web.dto.DiaryFetchResponseDTO;
import kr.co.ssalon.web.dto.DiaryInitResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryService {

    @Value("${spring.cloud.aws.s3.endpoint}")
    private String AWS_S3_ASSET_URI;
    private final String DIARY_TEMPLATE_FOLDER = "DIARY-TEMPLATE";

    @Autowired
    private AwsS3Service awsS3Service;

    public DiaryFetchResponseDTO fetchDiary(Long moimID, String userEmail) {
        // R of CRUD : 다이어리 조회

        String resultJSON = awsS3Service.getFileAsJsonString(moimID.toString(), userEmail);

        return DiaryFetchResponseDTO.builder()
                .resultCode("200 OK")
                .resultJSON(resultJSON)
                .build();
    }

    public DiaryInitResponseDTO initDiary(Long moimID, String userEmail) {
        // C of CRUD : 다이어리 생성
        // 템플릿 복제하여 최초 다이어리 생성
        String diaryJsonString = awsS3Service.getFileAsJsonString(DIARY_TEMPLATE_FOLDER);

        // JSON 내 src 구주소:신주소 기록용 Map 생성
        Map<String, String> imageSrcMap = new HashMap<>();

        // JSON 파싱 및 수정 작업
        String toLocation = moimID.toString() + "/" + userEmail;
        JsonElement jsonElement = editDiaryJsonSrc(DIARY_TEMPLATE_FOLDER, toLocation, diaryJsonString, imageSrcMap);

        // 이제 수정된 JSON 업로드 필요
        // 이후 JSON 내용과 동일하게 이미지 파일 복제 및 이름 변경 작업 진행
        // ** 추후 고려 : 중간에 실패한다면? **
        String resultJson = awsS3Service.uploadFileViaStream(moimID, userEmail, jsonElement.toString());   // JSON 업로드
        List<String> resultCopy = awsS3Service.copyFilesFromTemplate(imageSrcMap);              // JSON 기반 정적 파일 복제

        return DiaryInitResponseDTO.builder()
                .resultCode("201 Created")
                .resultJsonUpload(resultJson)
                .resultCopyFiles(resultCopy)
                .build();
    }

    public String editDiary(Long moimId, String userEmail, String json) {
        // 주어진 모임ID 바탕으로 다이어리 업로드 내용 수정
        // 현재(240512)는 기존 내용 삭제 후 새로 업로드
        // 추후 : 이전 다이어리 수정 기록 보존을 고려할 것

        // 임시 정책 : 일단 기존 파일은 내버려두고, 새 파일 생성
        // 추후 고려 정책 : 먼저, 기존 파일들 전부 삭제

        // JSON src 수정 후 업로드 진행
        Map<String, String> imageSrcMap = new HashMap<>();
        JsonElement jsonElement = editDiaryJsonSrc(moimId.toString(), moimId.toString(), json, imageSrcMap);
        String resultJson = awsS3Service.uploadFileViaStream(moimId, userEmail, jsonElement.toString());

        // 결과 반환
        return null; //new TicketEditResponseDTO(resultJson, jsonElement.toString());
    }

    private JsonElement editDiaryJsonSrc(String fromMoimId, String toMoimId, String jsonStr, Map<String, String> imageSrcMap) {
        // 목표 : 키 파싱 설계를 좀더 Portable 하게 수정하기

        // JSON 파일 이름 대조하여 변경 작업
        JsonElement jsonElement = JsonParser.parseString(jsonStr);
        JsonObject topLevelObject = jsonElement.getAsJsonObject();

        // thumbnail 이미지 파일 이름 수정
        String urlThumb = topLevelObject.get("thumbnailUrl").getAsString();
        String extFile = urlThumb.substring(urlThumb.lastIndexOf('.') + 1);
        String newThumbURI = "Thumbnails/" + toMoimId + "/Thumb-" + toMoimId + "." + extFile;
        String oldThumbURI = "Thumbnails/" + fromMoimId + "/" + urlThumb.substring(urlThumb.lastIndexOf('/') + 1);

        imageSrcMap.put(oldThumbURI, newThumbURI);
        topLevelObject.addProperty("thumbnailUrl", AWS_S3_ASSET_URI + newThumbURI);

        return jsonElement;
    }
}
