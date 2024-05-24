package kr.co.ssalon.web.controller;

import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.domain.service.DiaryService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

// DiaryController : 증표 뒷면(일기) 관련 API
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping("/{moimId}") // R of CRUD : 다이어리 조회
    public ResponseEntity<DiaryFetchResponseDTO> fetchDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        DiaryFetchResponseDTO diaryFetchResponseDTO = diaryService.fetchDiary(moimId, username);

        if (diaryFetchResponseDTO.getResultCode().equals("200 OK")) return ResponseEntity.ok(diaryFetchResponseDTO);
        else return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{moimId}") // C of CRUD : 다이어리 생성
    public ResponseEntity<String> createDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        DiaryInitResponseDTO diaryInitResponseDTO = diaryService.initDiary(moimId, username);

        if (diaryInitResponseDTO.getResultCode().equals("201 Created")) return ResponseEntity.created(URI.create("/"+moimId+"/"+username)).body(diaryInitResponseDTO.toString());
        else return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{moimId}") // U of CRUD : 다이어리 편집
    public ResponseEntity<TicketEditResponseDTO> editDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @RequestPart String json) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        TicketEditResponseDTO ticketEditResponseDTO = diaryService.editDiary(moimId, username, json);

        if (ticketEditResponseDTO.getResultJson().equals("200 OK")) return ResponseEntity.ok(ticketEditResponseDTO);
        else return ResponseEntity.badRequest().body(ticketEditResponseDTO);
    }

    @PostMapping("/{moimId}/image") // U of CRUD : 다이어리 편집을 위한 이미지 업로드
    public ResponseEntity<TicketImageResponseDTO> uploadDiaryImage(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @RequestPart List<MultipartFile> files) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        TicketImageResponseDTO ticketImageResponseDTO = diaryService.uploadImages(moimId, files);

        return switch (ticketImageResponseDTO.getResultCode()) {
            case "201 Created" -> ResponseEntity.status(HttpStatus.CREATED).body(ticketImageResponseDTO);
            case "206 Partial Content" -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(ticketImageResponseDTO);
            case "502 Bad Gateway" -> ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ticketImageResponseDTO);
            default -> ResponseEntity.badRequest().body(ticketImageResponseDTO); // 400 Bad Request
        };
    }

    @DeleteMapping("/{moimId}") // D of CRUD : 다이어리 삭제
    public ResponseEntity<String> deleteDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        return null;
    }

    @GetMapping("/{moimId}/info")
    public ResponseEntity<DiaryInfoDTO> fetchDiaryInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        return ResponseEntity.ok(diaryService.fetchDiaryInfo(moimId, customOAuth2Member.getUsername()));
    }

    @PostMapping("/{moimId}/info")
    public ResponseEntity<String> updateDiaryInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @ModelAttribute DiaryInfoDTO diaryInfoDTO) {

        String result = diaryService.updateDiaryInfo(moimId, customOAuth2Member.getUsername(), diaryInfoDTO).toString();
        return ResponseEntity.ok(result);
    }

    private String usernameConverter(String originString) {
        return originString.replace(" ", "-");
    }
}
