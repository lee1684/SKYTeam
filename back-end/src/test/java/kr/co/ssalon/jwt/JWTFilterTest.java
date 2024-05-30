package kr.co.ssalon.jwt;

import jakarta.servlet.FilterChain;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class JWTFilterTest {

    JWTUtil jwtUtil = new JWTUtil("ddddddddddbbbbbbbbbbbbcccccccccccc");

    JWTFilter jwtFilter = new JWTFilter(jwtUtil);

    static String username = "";
    static String role = "";

    @BeforeEach
    public void getUsernameAndRole() {
        // 현재 로그인된 유저(@WithCustomMockUser)의 username, role 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // username 추출
        username = authentication.getName();

        // role 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        role = auth.getAuthority();
    }

    @Test
    @WithCustomMockUser(username = "naver", role = "ROLE_USER")
    public void jwtFilter진행확인() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // 유효한 JWT 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        request.addHeader("Authorization", "Bearer " + access);

        // when (jwtFilter 필터 진행)
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then (authentication에 유저 세션이 저장되었는지 확인)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // username 추출
        String username = authentication.getName();
        // role 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String extractedRole = authorities.iterator().next().getAuthority();

        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getName()).isEqualTo(username);
        assertThat(extractedRole).isEqualTo(role);

        // filterChain.doFilter(=다음 필터로 진행)가 호출되었는지 확인
        Mockito.verify(filterChain).doFilter(request, response);
    }
}
