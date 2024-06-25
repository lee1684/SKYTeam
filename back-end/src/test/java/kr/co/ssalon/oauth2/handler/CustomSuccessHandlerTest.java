package kr.co.ssalon.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CustomSuccessHandlerTest {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RedisRefreshTokenRepository redisRefreshTokenRepository;

    @InjectMocks
    private CustomSuccessHandler customSuccessHandler;

    Authentication authentication = null;

    static String username = "";
    static String role = "";

    @BeforeEach
    public void getUsernameAndRole() {
        // 소셜로부터 가져온 유저(@WithCustomMockUser)의 username, role 가져오기
        authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2Member customOAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        // username 추출
        username = customOAuth2Member.getUsername();

        // role 추출
        Collection<? extends GrantedAuthority> authorities = customOAuth2Member.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        role = auth.getAuthority();
    }

    @Test
    @WithCustomMockUser
    public void 토큰response() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        // JWTUtil stub
        String access = "mock_access_token";
        String refresh = "mock_refresh_token";
        when(jwtUtil.createJwt("access", username, role, 600000L)).thenReturn(access);
        when(jwtUtil.createJwt("refresh", username, role, 86400000L)).thenReturn(refresh);

        // response.getWriter() stub
        when(response.getWriter()).thenReturn(writer);

        // when
        customSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpStatus.OK.value());
        verify(writer).print("{\"access\":\"" + access + "\",\"refresh\":\"" + refresh + "\"}");
        verify(writer).flush();
    }
}
