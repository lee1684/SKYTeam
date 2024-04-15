package kr.co.ssalon.oauth2.oauthService;

import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.*;
import kr.co.ssalon.web.dto.Oauth2MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberService memberService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String role = "ROLE_USER";
        Oauth2MemberDto oauth2MemberDto = new Oauth2MemberDto(username, oAuth2Response.getEmail(), role);
        try {
            memberService.register(oauth2MemberDto.getUsername(), oauth2MemberDto.getEmail(), oauth2MemberDto.getRole());
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            memberService.oauthUpdate(oauth2MemberDto.getUsername(), oauth2MemberDto.getEmail(), oauth2MemberDto.getRole());
        }
        return new CustomOAuth2Member(oauth2MemberDto);
    }
}
