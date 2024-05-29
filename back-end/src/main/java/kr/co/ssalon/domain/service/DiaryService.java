package kr.co.ssalon.domain.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.co.ssalon.domain.entity.Diary;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.DiaryRepository;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class DiaryService {

    @Value("${spring.cloud.aws.s3.endpoint}")
    private String AWS_S3_ASSET_URI;
    private final String DIARY_TEMPLATE_FOLDER = "DIARY-TEMPLATE";

    @Autowired
    private AwsS3Service awsS3Service;
    @Autowired
    private MemberMeetingRepository memberMeetingRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MeetingRepository meetingRepository;

    public DiaryFetchResponseDTO fetchDiary(Long moimID, String username) {
        // R of CRUD : 다이어리 조회

        String resultJSON = awsS3Service.getFileAsJsonString(moimID.toString(), username);

        return DiaryFetchResponseDTO.builder()
                .resultCode("200 OK")
                .resultJSON(resultJSON)
                .build();
    }

    public DiaryInitResponseDTO initDiary(Long moimID, String username) {
        // C of CRUD : 다이어리 생성
        // 템플릿 복제하여 최초 다이어리 생성
        String diaryJsonString = awsS3Service.getFileAsJsonString(DIARY_TEMPLATE_FOLDER);

        // JSON 내 src 구주소:신주소 기록용 Map 생성
        Map<String, String> imageSrcMap = new HashMap<>();

        // JSON 파싱 및 수정 작업
        JsonElement jsonElement = editDiaryJsonSrc(DIARY_TEMPLATE_FOLDER, moimID.toString(), username, diaryJsonString, imageSrcMap);

        // 이제 수정된 JSON 업로드 필요
        // 이후 JSON 내용과 동일하게 이미지 파일 복제 및 이름 변경 작업 진행
        // ** 추후 고려 : 중간에 실패한다면? **
        String resultJson = awsS3Service.uploadFileViaStream(moimID, username, jsonElement.toString());   // JSON 업로드
        List<String> resultCopy = awsS3Service.copyFilesFromTemplate(imageSrcMap);              // JSON 기반 정적 파일 복제

        return DiaryInitResponseDTO.builder()
                .resultCode("201 Created")
                .resultJsonUpload(resultJson)
                .resultCopyFiles(resultCopy)
                .build();
    }

    public TicketEditResponseDTO editDiary(Long moimId, String username, String json) {
        // 주어진 모임ID 바탕으로 다이어리 업로드 내용 수정
        // 현재(240512)는 기존 내용 삭제 후 새로 업로드
        // 추후 : 이전 다이어리 수정 기록 보존을 고려할 것

        // 임시 정책 : 일단 기존 파일은 내버려두고, 새 파일 생성
        // 추후 고려 정책 : 먼저, 기존 파일들 전부 삭제

        // JSON src 수정 후 업로드 진행
        Map<String, String> imageSrcMap = new HashMap<>();
        JsonElement jsonElement = editDiaryJsonSrc(moimId.toString(), moimId.toString(), username, json, imageSrcMap);
        String resultJson = awsS3Service.uploadFileViaStream(moimId, username, jsonElement.toString());

        // 결과 반환
        return new TicketEditResponseDTO(resultJson, jsonElement.toString());
    }

    @Transactional
    public ImageResponseDTO uploadImages(Long moimId, List<MultipartFile> multipartFiles) {

        Map<String, String> imageSrcMap = new HashMap<>();

        int requestSize = multipartFiles.size();

        if (requestSize == 0) return ImageResponseDTO.builder()
                .resultCode("400 Bad Request")
                .numRequest(requestSize)
                .numResult(0)
                .mapURI(null)
                .build();

        for (MultipartFile multipartFile : multipartFiles) {
            String oldFileURI = multipartFile.getOriginalFilename();
            String extOldFile = oldFileURI.substring(oldFileURI.lastIndexOf('.') + 1);
            String newFileURI = moimId.toString() + "/" + generateRandomUUID() + "." + extOldFile;

            imageSrcMap.put(oldFileURI, newFileURI);
        }

        int resultSize = awsS3Service.uploadMultiFilesViaMultipart(multipartFiles, imageSrcMap);

        if (requestSize == resultSize) return ImageResponseDTO.builder()
                .resultCode("201 Created")
                .numRequest(requestSize)
                .numResult(resultSize)
                .mapURI(imageSrcMap)
                .build();
        else if (resultSize > 0) return ImageResponseDTO.builder()
                .resultCode("206 Partial Content")
                .numRequest(requestSize)
                .numResult(resultSize)
                .mapURI(imageSrcMap)
                .build();
        else return ImageResponseDTO.builder()
                    .resultCode("502 Bad Gateway")
                    .numRequest(requestSize)
                    .numResult(resultSize)
                    .mapURI(imageSrcMap)
                    .build();
    }

    public DiaryInfoDTO fetchDiaryInfo(Long moimId, String username) {

        Optional<Member> userOp = memberRepository.findByUsername(username);
        if (userOp.isEmpty()) {
            log.error("DiaryService(fetchDiaryInfo): Member not found");
            return null;
        }
        Optional<Meeting> meetingOp = meetingRepository.findById(moimId);
        if (meetingOp.isEmpty()) {
            log.error("DiaryService(fetchDiaryInfo): Meeting not found");
            return null;
        }

        Member user = userOp.get();
        Meeting meeting = meetingOp.get();

        Optional<MemberMeeting> memberMeetingOp = memberMeetingRepository.findByMemberAndMeeting(user, meeting);
        if (memberMeetingOp.isEmpty()) {
            log.error("DiaryService(fetchDiaryInfo): MemberMeeting not found");
            return null;
        }

        MemberMeeting memberMeeting = memberMeetingOp.get();
        Diary diary = memberMeeting.getDiary();
        log.info(diary.getDescription());

        if (diary.isEditYet()) return null;

        DiaryInfoDTO result = DiaryInfoDTO.builder()
                .description(diary.getDescription())
                .build();
        result.setDiaryPictureUrls(diary.getDiaryPictureUrls());
        return result;
    }

    public DiaryInfoDTO updateDiaryInfo(Long moimId, String username, DiaryInfoDTO diaryInfoDTO) {

        Optional<Member> userOp = memberRepository.findByUsername(username);
        if (userOp.isEmpty()) {
            log.error("DiaryService(updateDiaryInfo): Member not found");
            return null;
        }
        Optional<Meeting> meetingOp = meetingRepository.findById(moimId);
        if (meetingOp.isEmpty()) {
            log.error("DiaryService(updateDiaryInfo): Meeting not found");
            return null;
        }

        Member user = userOp.get();
        Meeting meeting = meetingOp.get();

        Optional<MemberMeeting> memberMeetingOp = memberMeetingRepository.findByMemberAndMeeting(user, meeting);
        if (memberMeetingOp.isEmpty()) {
            log.error("DiaryService(updateDiaryInfo): MemberMeeting not found");
            return null;
        }

        MemberMeeting memberMeeting = memberMeetingOp.get();
        Optional<Diary> diaryOp = diaryRepository.findByMemberMeetingId(memberMeeting.getId());
        if (diaryOp.isEmpty()) {
            log.error("DiaryService(updateDiaryInfo): Diary not found");
            return null;
        }
        Diary diary = diaryOp.get();

        diary.editDiaryDescription(diaryInfoDTO.getDescription());
        log.info("DiaryService(updateDiaryInfo): Diary updated - {}", diaryInfoDTO.getDescription());
        diary.addDiaryPictureUrls(diaryInfoDTO.getDiaryPictureUrls());

        Diary diaryAfter = diaryRepository.save(diary);

        DiaryInfoDTO resultDiaryInfoDTO = DiaryInfoDTO.builder()
                .description(diaryAfter.getDescription())
                .build();
        resultDiaryInfoDTO.setDiaryPictureUrls(diaryAfter.getDiaryPictureUrls());

        return resultDiaryInfoDTO;
    }

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private JsonElement editDiaryJsonSrc(String fromMoimId, String toMoimId, String username, String jsonStr, Map<String, String> imageSrcMap) {
        // 목표 : 키 파싱 설계를 좀더 Portable 하게 수정하기

        // JSON 파일 이름 대조하여 변경 작업
        JsonElement jsonElement = JsonParser.parseString(jsonStr);
        JsonObject topLevelObject = jsonElement.getAsJsonObject();

        // thumbnail 이미지 파일 이름 수정
        String urlThumb = topLevelObject.get("thumbnailUrl").getAsString();
        String extFile = urlThumb.substring(urlThumb.lastIndexOf('.') + 1);
        String newThumbURI = "Thumbnails/" + toMoimId + "/Thumb-" + username + "." + extFile;
        String oldThumbURI = "Thumbnails/" + fromMoimId + "/" + urlThumb.substring(urlThumb.lastIndexOf('/') + 1);

        imageSrcMap.put(oldThumbURI, newThumbURI);
        topLevelObject.addProperty("thumbnailUrl", AWS_S3_ASSET_URI + newThumbURI);

        return jsonElement;
    }
}
