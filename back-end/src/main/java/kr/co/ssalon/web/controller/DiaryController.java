package kr.co.ssalon.web.controller;

import kr.co.ssalon.domain.service.DiaryService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.DiaryFetchResponseDTO;
import kr.co.ssalon.web.dto.DiaryInitResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

// DiaryController : 증표 뒷면(일기) 관련 API
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping("/{moimId}/diary") // R of CRUD : 다이어리 조회
    public ResponseEntity<DiaryFetchResponseDTO> fetchDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {

        String userEmail = customOAuth2Member.getEmail();
        DiaryFetchResponseDTO diaryFetchResponseDTO = diaryService.fetchDiary(moimId, userEmail);

        if (diaryFetchResponseDTO.getResultCode().equals("200 OK")) return ResponseEntity.ok(diaryFetchResponseDTO);
        else return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{moimId}/diary") // C of CRUD : 다이어리 생성
    public ResponseEntity<String> createDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {

        String userEmail = customOAuth2Member.getEmail();
        DiaryInitResponseDTO diaryInitResponseDTO = diaryService.initDiary(moimId, userEmail);

        if (diaryInitResponseDTO.getResultCode().equals("201 Created")) return ResponseEntity.created(URI.create("/"+moimId+"/diary/"+userEmail)).body(diaryInitResponseDTO.toString());
        else return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{moimId}/diary") // U of CRUD : 다이어리 편집
    public ResponseEntity<String> editDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @RequestPart String json) {

        String userEmail = customOAuth2Member.getEmail();
        diaryService.editDiary(moimId, userEmail, json);

        return null;
    }

    @PostMapping("/{moimId}/diary/image") // U of CRUD : 다이어리 편집을 위한 이미지 업로드
    public ResponseEntity<String> uploadDiaryImage(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @RequestPart List<MultipartFile> files) {

        return null;
    }

    @DeleteMapping("/{moimId}/diary") // D of CRUD : 다이어리 삭제
    public ResponseEntity<String> deleteDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        return null;
    }
}
