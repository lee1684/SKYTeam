package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Diary;
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
@Tag(name = "다이어리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Operation(summary = "다이어리 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 조회 성공")
    })
    @GetMapping("/{moimId}") // R of CRUD : 다이어리 조회
    public ResponseEntity<DiaryFetchResponseDTO> fetchDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        DiaryFetchResponseDTO diaryFetchResponseDTO = diaryService.fetchDiary(moimId, username);

        if (diaryFetchResponseDTO.getResultCode().equals("200 OK")) return ResponseEntity.ok(diaryFetchResponseDTO);
        else return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "다이어리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 생성 성공")
    })
    @PostMapping("/{moimId}") // C of CRUD : 다이어리 생성
    public ResponseEntity<String> createDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        DiaryInitResponseDTO diaryInitResponseDTO = diaryService.initDiary(moimId, username);

        if (diaryInitResponseDTO.getResultCode().equals("201 Created")) return ResponseEntity.created(URI.create("/"+moimId+"/"+username)).body(diaryInitResponseDTO.toString());
        else return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "다이어리 편집")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 편집 성공")
    })
    @PutMapping("/{moimId}") // U of CRUD : 다이어리 편집
    public ResponseEntity<TicketEditResponseDTO> editDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @RequestPart String json) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        TicketEditResponseDTO ticketEditResponseDTO = diaryService.editDiary(moimId, username, json);

        if (ticketEditResponseDTO.getResultJson().equals("200 OK")) return ResponseEntity.ok(ticketEditResponseDTO);
        else return ResponseEntity.badRequest().body(ticketEditResponseDTO);
    }

    @Operation(summary = "다이어리 편집을 위한 이미지 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 편집을 위한 이미지 업로드 성공")
    })
    @PostMapping("/{moimId}/image") // U of CRUD : 다이어리 편집을 위한 이미지 업로드
    public ResponseEntity<ImageResponseDTO> uploadDiaryImage(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @RequestPart List<MultipartFile> files) {

        String username = usernameConverter(customOAuth2Member.getUsername());
        ImageResponseDTO imageResponseDTO = diaryService.uploadImages(moimId, files);

        return switch (imageResponseDTO.getResultCode()) {
            case "201 Created" -> ResponseEntity.status(HttpStatus.CREATED).body(imageResponseDTO);
            case "206 Partial Content" -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(imageResponseDTO);
            case "502 Bad Gateway" -> ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(imageResponseDTO);
            default -> ResponseEntity.badRequest().body(imageResponseDTO); // 400 Bad Request
        };
    }

    @Operation(summary = "다이어리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 삭제 성공")
    })
    @DeleteMapping("/{moimId}") // D of CRUD : 다이어리 삭제
    public ResponseEntity<String> deleteDiary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        return null;
    }

    @GetMapping("/{moimId}/info")
    public ResponseEntity<DiaryInfoDTO> fetchDiaryInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        DiaryInfoDTO diaryInfoDTO = diaryService.fetchDiaryInfo(moimId, customOAuth2Member.getUsername());

        if (diaryInfoDTO.getDescription() == null || diaryInfoDTO.getDescription().equals("NOT EDIT YET")) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(diaryInfoDTO);
    }

    @PostMapping("/{moimId}/info")
    public ResponseEntity<Diary> updateDiaryInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @ModelAttribute DiaryInfoDTO diaryInfoDTO) {

        Diary result = diaryService.updateDiaryInfo(moimId, customOAuth2Member.getUsername(), diaryInfoDTO);
        return ResponseEntity.ok(result);
    }

    private String usernameConverter(String originString) {
        return originString.replace(" ", "-");
    }
}
