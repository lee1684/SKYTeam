package kr.co.ssalon.kakaopay;

import kr.co.ssalon.kakaopay.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@RequiredArgsConstructor
public class PayService {
    private final KakaoPayProperties kakaoPayProperties;

    public KakaopayReadyResponseDto kakaoPayReady(KakaopayReadyRequestDto request) {

        HttpEntity<LinkedHashMap<String, String>> requestEntity = new HttpEntity<>(this.getReadyParameters(request), this.getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        KakaopayReadyResponseDto response = restTemplate.postForObject(
                kakaoPayProperties.getReadyUrl(),
                requestEntity,
                KakaopayReadyResponseDto.class
        );
        return response;
    }

    public KakaopayApproveResponseDto kakaoPayApprove(KakaopayApproveRequestDto request) {

        HttpEntity<LinkedHashMap<String, String>> requestEntity = new HttpEntity<>(this.getApproveParameters(request), this.getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        KakaopayApproveResponseDto response = restTemplate.postForObject(
                kakaoPayProperties.getApproveUrl(),
                requestEntity,
                KakaopayApproveResponseDto.class
        );
        return response;
    }

    public KakaopayCancelResponseDto kakaoPayCancel(KakaopayCancelRequestDto request) {

        HttpEntity<LinkedHashMap<String, String>> requestEntity = new HttpEntity<>(this.getCancelParameters(request), this.getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        KakaopayCancelResponseDto response = restTemplate.postForObject(
                kakaoPayProperties.getCancelUrl(),
                requestEntity,
                KakaopayCancelResponseDto.class
        );
        return response;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + kakaoPayProperties.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private LinkedHashMap<String, String> getReadyParameters(KakaopayReadyRequestDto request) {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("cid", kakaoPayProperties.getCid());
        parameters.put("partner_order_id", request.getPartnerOrderId());
        parameters.put("partner_user_id", request.getPartnerUserId());
        parameters.put("item_name", request.getItemName());
        parameters.put("quantity", String.valueOf(request.getQuantity()));
        parameters.put("total_amount", String.valueOf(request.getTotalPayment()));
        parameters.put("tax_free_amount", String.valueOf(request.getTaxFreeAmount()));
        parameters.put("approval_url", request.getApprovalUrl());
        parameters.put("cancel_url", request.getCancelUrl());
        parameters.put("fail_url", request.getFailUrl());
        return parameters;
    }

    private LinkedHashMap<String, String> getApproveParameters(KakaopayApproveRequestDto request) {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("tid", request.getTid());
        parameters.put("cid", kakaoPayProperties.getCid());
        parameters.put("partner_order_id", request.getPartnerOrderId());
        parameters.put("partner_user_id", request.getPartnerUserId());
        parameters.put("pg_token", request.getPgToken());
        return parameters;
    }

    private LinkedHashMap<String, String> getCancelParameters(KakaopayCancelRequestDto request) {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("tid", request.getTid());
        parameters.put("cid", kakaoPayProperties.getCid());
        parameters.put("cancel_amount", String.valueOf(request.getCancelAmount()));
        parameters.put("cancel_tax_free_amount", String.valueOf(request.getCancelTaxFreeAmount()));

        return parameters;
    }
}
