package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.service.ImageUploadService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.ImageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "이미지 업로드")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image-upload")
public class ImageUploadController {

    @Autowired
    private final ImageUploadService imageUploadService;

    @Operation(summary = "개인 이미지 업로드를 위한 범용 API")
    @ApiResponse(responseCode = "201", description = "이미지 업로드 성공", content = {
            @Content(schema = @Schema(implementation = ImageResponseDTO.class))
    })
    @PostMapping("/general")
    public ResponseEntity<ImageResponseDTO> uploadImage(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestPart List<MultipartFile> files) {

        String username = customOAuth2Member.getUsername();

        ImageResponseDTO imageResponseDTO = imageUploadService.uploadImage(username, files);

        return switch (imageResponseDTO.getResultCode()) {
            case "201 Created" -> ResponseEntity.status(HttpStatus.CREATED).body(imageResponseDTO);
            case "206 Partial Content" -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(imageResponseDTO);
            case "502 Bad Gateway" -> ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(imageResponseDTO);
            default -> ResponseEntity.badRequest().body(imageResponseDTO); // 400 Bad Request
        };
    }
}
