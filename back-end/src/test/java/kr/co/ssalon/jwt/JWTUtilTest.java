package kr.co.ssalon.jwt;

import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WithCustomMockUser(username = "naver", role = "ROLE_USER")
public class JWTUtilTest {

    JWTUtil jwtUtil = new JWTUtil("ddddddddddbbbbbbbbbbbbcccccccccccc");

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
    @DisplayName("JWTUtil.getUsername() 테스트")
    public void getUsername() {
        // given

        // access token 생성
        String token = jwtUtil.createJwt("access", username, role, 6000000L);

        // when (username 추출)
        String extractedUsername = jwtUtil.getUsername(token);

        // then
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    @DisplayName("JWTUtil.getRole() 테스트")
    public void getRole() {
        // given

        // access token 생성
        String token = jwtUtil.createJwt("access", username, role, 6000000L);

        // when (role 추출)
        String extractedRole = jwtUtil.getRole(token);

        // then
        assertThat(extractedRole).isEqualTo(role);
    }

    @Test
    @DisplayName("JWTUtil.getCategory() 테스트")
    public void getCategory() {
        // given

        // access token 생성
        String category = "access";
        String token = jwtUtil.createJwt(category, username, role, 6000000L);

        // when (category 테스트)
        String extractedCategory = jwtUtil.getCategory(token);

        // then
        assertThat(extractedCategory).isEqualTo(category);
    }

    @Test
    @DisplayName("JWTUtil.isExpired() 테스트")
    public void isExpired() {
        // given

        // access token 생성
        String token = jwtUtil.createJwt("access", username, role, 6000000L);

        // when (isExpired 테스트)
        Boolean isExpired = jwtUtil.isExpired(token);

        // then
        assertThat(isExpired).isEqualTo(false);
    }

    @Test
    @DisplayName("JWTUtil.createJwt() 테스트")
    public void createJwt() {
        // given

        // when (createJwt 테스트)
        String token = jwtUtil.createJwt("access", username, role, 6000000L);

        // then
        assertThat(jwtUtil.getUsername(token)).isEqualTo(username);
        assertThat(jwtUtil.getRole(token)).isEqualTo(role);

    }
}
