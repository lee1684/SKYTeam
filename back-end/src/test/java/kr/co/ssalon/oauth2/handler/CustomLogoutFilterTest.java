package kr.co.ssalon.oauth2.handler;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshToken;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.ServletException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomLogoutFilterTest {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RedisRefreshTokenRepository redisRefreshTokenRepository;

    @InjectMocks
    private CustomLogoutFilter customLogoutFilter;

    @Test
    @DisplayName("로그아웃 API (/api/auth/logout) 테스트")
    void 로그아웃진행() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        request.setRequestURI("/api/auth/logout");
        request.setMethod("DELETE");
        request.addHeader("Refresh", "valid_refresh_token");

        // RedisRefreshTokenRespository stub
        RedisRefreshToken redisRefreshToken = new RedisRefreshToken();
        when(redisRefreshTokenRepository.existsByRefresh(anyString())).thenReturn(true);
        when(redisRefreshTokenRepository.findByRefresh(anyString())).thenReturn(redisRefreshToken);

        // JWTUtil stub
        when(jwtUtil.getCategory(anyString())).thenReturn("refresh");

        // when (로그아웃 필터 진행 확인)
        customLogoutFilter.doFilter(request, response, filterChain);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

        // delete() 호출 여부 확인
        verify(redisRefreshTokenRepository).delete(redisRefreshToken);
    }
}
