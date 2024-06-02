package kr.co.ssalon.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ssalon.web.dto.ImageGenerationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGptService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/images/generations";
    private final ObjectMapper objectMapper;
    private final TranslationService translationService;
    private final AwsS3Service awsS3Service;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public String generateAndResizeImage(Long moimId, ImageGenerationDTO imageGenerationDTO) throws IOException {
        String prompt = imageGenerationDTO.getPrompt();
        Boolean highQuality = imageGenerationDTO.getHighQuality();
        Integer imageSize = highQuality ? 1024 : 512;

        String translatedText = translationService.translateText(prompt, "ko", "en");
        log.info("translatedText = {}", translatedText);

        Map<String, Object> requestHashMap = new HashMap<>();
        requestHashMap.put("prompt", translatedText);
        requestHashMap.put("n", 1);
        requestHashMap.put("size", String.format("%dx%d", imageSize, imageSize));
        if (highQuality) {
            requestHashMap.put("model", "dall-e-3");
            requestHashMap.put("quality", "hd");
        }

        String requestBodyJson = objectMapper.writeValueAsString(requestHashMap);

        RequestBody body = RequestBody.create(
                requestBodyJson,
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String jsonData = response.body().string();
        log.info("openai api response = {}", jsonData);
        String imageUrl = extractImageUrlFromResponse(jsonData);

        byte[] imageBytes = downloadImage(imageUrl);

        return awsS3Service.uploadFileViaByteArray(moimId, imageBytes);
    }

    private String extractImageUrlFromResponse(String jsonData) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode dataNode = rootNode.path("data");
        if (dataNode.isArray() && dataNode.size() > 0) {
            JsonNode firstImageNode = dataNode.get(0);
            JsonNode urlNode = firstImageNode.path("url");
            if (urlNode.isTextual()) {
                return urlNode.asText();
            }
        }
        throw new IOException("Invalid JSON response or missing URL");
    }

    private byte[] downloadImage(String imageUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(imageUrl).build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return IOUtils.toByteArray(response.body().byteStream());
    }

    private byte[] resizeImage(byte[] originalImageBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(originalImageBytes);
        BufferedImage originalImage = ImageIO.read(bais);

        BufferedImage resizedImage = new BufferedImage(350, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 350, 600, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", baos);
        return baos.toByteArray();
    }
}
