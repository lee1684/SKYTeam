package kr.co.ssalon.web.controller;
import kr.co.ssalon.domain.service.ChatGptService;
import kr.co.ssalon.web.dto.ImageGenerationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;

    @PostMapping("/api/image/generate/{moimId}")
    public String generateImage(@RequestBody ImageGenerationDTO imageGenerationDTO, @PathVariable Long moimId) throws IOException {
//        return chatGptService.generateAndResizeImage(moimId, imageGenerationDTO);
        return chatGptService.generateByKarlo(moimId, imageGenerationDTO);
    }
}
