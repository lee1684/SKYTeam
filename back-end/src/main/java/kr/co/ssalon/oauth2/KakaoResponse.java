package kr.co.ssalon.oauth2;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attriubute;

    public KakaoResponse(Map<String, Object> attriubute) {
        this.attriubute = attriubute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attriubute.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attriubute.get("kakao_account");
        return kakaoAccount.get("email").toString();
    }


}
