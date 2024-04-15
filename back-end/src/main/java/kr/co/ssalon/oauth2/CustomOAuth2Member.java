package kr.co.ssalon.oauth2;


import kr.co.ssalon.web.dto.Oauth2MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {

    private final Oauth2MemberDto oauth2MemberDto;


    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return oauth2MemberDto.getRole();
            }
        });
        return collection;
    }

    public String getUsername() {
        return oauth2MemberDto.getUsername();
    }


    @Override
    public String getName() {
        return "default";
    }
}
