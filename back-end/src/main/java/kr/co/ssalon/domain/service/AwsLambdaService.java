package kr.co.ssalon.domain.service;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class AwsLambdaService {

    private final WebClient webClient;

    public AwsLambdaService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://ssalon.co.kr") // 기본 URL 설정
                .build();
    }

    public Mono<String> fetchMoimRecommendation(Long userID) {
       return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/embedding/search")
                        .queryParam("userID", userID)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> fetchCategoryRecommendation(Long userID) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/embedding/search/category")
                        .queryParam("userID", userID)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public void updateUserEmbedding(Long userID, String username, String prompt) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userID);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("prompt", prompt);

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/embedding/user")
                        .build())
                .bodyValue(jsonObject.toString())
                .retrieve();
    }

    public void updateMoimEmbedding(Long moimID, String moimTitle, String prompt) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("moimId", moimID);
        jsonObject.addProperty("moimTitle", moimTitle);
        jsonObject.addProperty("prompt", prompt);

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/embedding/moim")
                        .build())
                .bodyValue(jsonObject.toString())
                .retrieve();
    }

    public void updateCategoryEmbedding(Long categoryID, String categoryName, String prompt) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("categoryId", categoryID);
        jsonObject.addProperty("category", categoryName);
        jsonObject.addProperty("prompt", prompt);

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/embedding/category")
                        .build())
                .bodyValue(jsonObject.toString())
                .retrieve();
    }
}
