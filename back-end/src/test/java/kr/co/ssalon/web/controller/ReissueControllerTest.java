package kr.co.ssalon.web.controller;

import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import kr.co.ssalon.web.controller.oauth2.ReissueController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collection;
import java.util.Iterator;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReissueController.class)
public class ReissueControllerTest {

    @MockBean
    private RedisRefreshTokenRepository redisRefreshTokenRepository;

    @MockBean
    JWTUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    String username = "";
    String role = "";

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
    @DisplayName("access 토큰 재발급 API(POST /api/reissue) 테스트")
    @WithCustomMockUser(username = "user1", role = "ROLE_USER")
    public void access토큰재발급() throws Exception {
        // given

        // JwtUtil stub
        String previousRefresh = "previous refresh token";
        String newAccess = "new access token";
        String newRefresh = "new refresh token";
        when(jwtUtil.isExpired(previousRefresh)).thenReturn(false);
        when(jwtUtil.getCategory(previousRefresh)).thenReturn("refresh");
        when(jwtUtil.getUsername(previousRefresh)).thenReturn(username);
        when(jwtUtil.getRole(previousRefresh)).thenReturn(role);
        when(jwtUtil.createJwt("access", username, role, 600000L)).thenReturn(newAccess);
        when(jwtUtil.createJwt("refresh", username, role, 86400000L)).thenReturn(newRefresh);

        // RedisRefreshTokenRepository.existsByRefresh() stub
        when(redisRefreshTokenRepository.existsByRefresh(previousRefresh)).thenReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/reissue")
                .with(csrf())
                .header("Refresh", previousRefresh));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.access", is(newAccess)))
                .andExpect(jsonPath("$.refresh", is(newRefresh)));
    }
}
