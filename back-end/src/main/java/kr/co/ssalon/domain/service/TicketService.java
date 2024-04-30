package kr.co.ssalon.domain.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.co.ssalon.web.dto.TicketEditResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class TicketService {

    @Value("${spring.cloud.aws.s3.endpoint}")
    private String AWS_S3_ASSET_URI;
    private final static String TEMPLATE_FOLDER = "Template-240424";

    @Autowired
    AwsS3Service awsS3Service;

    @Transactional
    public String initTicket(Long moimId) {
        // 템플릿 복제 후 해당 경로 반환
        // JSON 복제 후, 해당 JSON 내 파일명에 맞게 나머지 복제 필요
        // 따라서, JSON 복제 과정에서 신규 파일명 명명 후 해당 파일명 Key-Value 형태로 넘겨줘야 할 것

        // 먼저, JSON 복제 및 내부 파일 링크 추출 및 수정
        String jsonStr = awsS3Service.getFileAsJsonString(TEMPLATE_FOLDER);

        // JSON 내 src 구주소:신주소 기록용 Map 생성
        Map<String, String> imageSrcMap = new HashMap<>();

        // JSON 파싱 및 수정 작업
        JsonElement jsonElement = editTicketJsonSrc(moimId, jsonStr, imageSrcMap);

        // 이제 수정된 JSON 업로드 필요
        // 이후 JSON 내용과 동일하게 이미지 파일 복제 및 이름 변경 작업 진행
        // ** 추후 고려 : 중간에 실패한다면? **
        awsS3Service.uploadFileViaStream(moimId, jsonElement.toString());                       // JSON 업로드
        awsS3Service.copyFilesFromTemplate(TEMPLATE_FOLDER, moimId.toString(), imageSrcMap);    // JSON 기반 정적 파일 복제

        return jsonElement.toString();
    }

    public String loadTicket(Long moimId) {
        // 주어진 모임ID 바탕으로 티켓 정보 반환
        // 현재로썬 링크 양식이 정해져 있으니 그냥 바로 로드 가능하지 않을까...?
        // 의문 : JSON은 가능한 숨기는 게 좋을까?
        return awsS3Service.getFileAsJsonString(moimId.toString());
    }

    @Transactional
    public TicketEditResponseDTO editTicket(Long moimId, String json, List<MultipartFile> multipartFiles, MultipartFile thumbnail) {
        // 주어진 모임ID 바탕으로 티켓 업로드 내용 수정
        // 현재(240424)는 기존 내용 삭제 후 새로 업로드
        // 추후 : 이전 티켓 수정 기록 보존을 고려할 것

        // 임시 정책 : 일단 기존 파일은 내버려두고, 새 파일 생성
        // 추후 고려 정책 : 먼저, 기존 파일들 전부 삭제

        // JSON src 수정 후 업로드 진행
        Map<String, String> imageSrcMap = new HashMap<>();
        JsonElement jsonElement = editTicketJsonSrc(moimId, json, imageSrcMap);
        String resultJson = awsS3Service.uploadFileViaStream(moimId, jsonElement.toString());

        // 파일 업로드 진행
        List<String> resultSrc = awsS3Service.uploadMultiFilesViaMultipart(moimId, multipartFiles, imageSrcMap);
        resultSrc.add(awsS3Service.uploadSingleFileViaMultipart(moimId, thumbnail, imageSrcMap));

        // 결과 반환
        return new TicketEditResponseDTO(resultJson, jsonElement.toString(), resultSrc);
    }

    @Transactional
    public void deleteTicket() {
        // 주어진 모임ID의 S3 파일 삭제
        // 해당 모임ID 티켓 JSON 파일 내 Objects의 Type: image의 Link 추출 후 Key 리스트 작성
        // 완성된 Key 리스트를 S3에 전달
    }

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private JsonElement editTicketJsonSrc(Long moimId, String jsonStr, Map<String, String> imageSrcMap) {
        // JSON 파일 이름 대조하여 변경 작업
        JsonElement jsonElement = JsonParser.parseString(jsonStr);
        JsonObject topLevelObject = jsonElement.getAsJsonObject();

        // thumbnail 이미지 파일 이름 수정
        String uuidThumb = generateRandomUUID();
        String urlThumb = topLevelObject.get("thumbnailUrl").getAsString();
        String urlThumbSrc = urlThumb.substring(urlThumb.lastIndexOf('/') + 1);

        imageSrcMap.put(urlThumbSrc, uuidThumb);
        topLevelObject.addProperty("thumbnailUrl", AWS_S3_ASSET_URI + "Thumbnails/" + moimId + "/" + uuidThumb + ".png");

        JsonObject fabricObject = topLevelObject.get("fabric").getAsJsonObject();
        JsonArray objectsArray = fabricObject.get("objects").getAsJsonArray();

        // objects 리스트 내 Image 파일에 대해서만 작업 진행
        for (JsonElement object : objectsArray) {
            JsonObject objCandidate = object.getAsJsonObject();
            if (objCandidate.get("type").getAsString().equals("image")) {
                String uuid = generateRandomUUID();
                String urlOrigin = objCandidate.get("src").getAsString();
                String urlSrc = urlOrigin.substring(urlOrigin.lastIndexOf('/') + 1);

                imageSrcMap.put(urlSrc, uuid);
                objCandidate.addProperty("src", AWS_S3_ASSET_URI + moimId + "/" + uuid + ".png");
            }
        }
        return jsonElement;
    }
}
