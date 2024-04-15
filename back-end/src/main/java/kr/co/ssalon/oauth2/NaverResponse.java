package kr.co.ssalon.oauth2;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attriubute;

    public NaverResponse(Map<String, Object> attriubute) {
        this.attriubute = (Map<String, Object>) attriubute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attriubute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attriubute.get("email").toString();
    }


}
