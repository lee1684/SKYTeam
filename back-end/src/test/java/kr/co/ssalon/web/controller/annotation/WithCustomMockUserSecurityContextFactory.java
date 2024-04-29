package kr.co.ssalon.web.controller.annotation;

import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.Oauth2MemberDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser withCustomMockUser) {
        String username = withCustomMockUser.username();
        String email = withCustomMockUser.email();
        String role = withCustomMockUser.role();

        Oauth2MemberDto oauth2MemberDto = Oauth2MemberDto.builder()
                .username(username)
                .role(role)
                .email(email)
                .build();
        CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(oauth2MemberDto);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customOAuth2Member, null, customOAuth2Member.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
        return context;
    }
}
