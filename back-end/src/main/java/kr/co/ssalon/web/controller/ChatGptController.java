package kr.co.ssalon.web.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.service.ChatGptService;
import kr.co.ssalon.web.dto.ImageGenerationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "AI 이미지")
@RestController
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;

    @Operation(summary = "AI를 통한 이미지 생성")
    @ApiResponse(responseCode = "201", description = "이미지 생성 성공", content = {
            @Content(schema = @Schema(implementation = String.class))
    })
    @PostMapping("/api/image/generate/{moimId}")
    public String generateImage(@RequestBody ImageGenerationDTO imageGenerationDTO, @PathVariable Long moimId) throws IOException {
//        return chatGptService.generateAndResizeImage(moimId, imageGenerationDTO);
        return chatGptService.generateByKarlo(moimId, imageGenerationDTO);
    }
}
