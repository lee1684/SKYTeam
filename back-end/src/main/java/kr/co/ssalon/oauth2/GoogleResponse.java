package kr.co.ssalon.oauth2;


import java.util.Map;

public class GoogleResponse implements OAuth2Response {
    private final Map<String, Object> attriubute;

    public GoogleResponse(Map<String, Object> attriubute) {
        this.attriubute = attriubute;
    }


    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attriubute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attriubute.get("email").toString();
    }

}
