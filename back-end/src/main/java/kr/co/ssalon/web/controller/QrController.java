package kr.co.ssalon.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.service.QrService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.QrLinkDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "QR")
@Slf4j
@RestController
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    @Operation(summary = "QR 생성/조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "200", description = "QR 생성/조회 성공"),
    })
    @GetMapping("/api/tickets/{moimId}/link")
    public ResponseEntity<?> getQrLink(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        try {
            String username = customOAuth2Member.getUsername();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrService.getQrLink(username, moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "QR 검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "200", description = "QR 검증 성공"),
    })
    @PostMapping("/api/tickets/{moimId}/link")
    public ResponseEntity<?> checkQrLink(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @RequestBody QrLinkDTO qrLinkDTO) {
        try {
            return ResponseEntity.ok().body(qrService.checkQrLink(moimId, qrLinkDTO.getQrKey()));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}