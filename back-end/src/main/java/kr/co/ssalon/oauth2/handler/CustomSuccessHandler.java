package kr.co.ssalon.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshToken;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomSuccessHandler");
        CustomOAuth2Member customOAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();
        String username = customOAuth2Member.getUsername();

//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();
        String role = memberRepository.findByUsername(username).get().getRole();

        String access = jwtUtil.createJwt("access", username, role, 86400000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        addRedisRefreshEntity(username, refresh);

        // response에 쿠키 추가
        Cookie accessCookie = createCookieAccess("access", access);
        Cookie refreshCookie = createCookieRefresh("refresh", refresh);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        // redirect 경로 설정
        if (Objects.equals(role, "ROLE_ADMIN")) {
            log.info("@@@@@@@@@@@@@@@@@if문 role: {}", role);
            response.sendRedirect("https://ssalon.co.kr/admin");
        } else {
            log.info("@@@@@@@@@@@@@@@@@else문 role: {}", role);
            response.sendRedirect("https://ssalon.co.kr/web/ssalon-login-redirect");
        }

        // JSON 객체 생성
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("access", access);
        jsonResponse.put("refresh", refresh);

        // response body의 MIME 타입 설정
        response.setContentType("application/json");

        // JSON response body로 쿠키 추가해서 전송
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();

        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookieAccess(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400000);
//        cookie.setSecure(true);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        return cookie;
    }

    private Cookie createCookieRefresh(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400000);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void addRedisRefreshEntity(String username, String refresh) {
        RedisRefreshToken redisRefreshToken = new RedisRefreshToken();
        redisRefreshToken.setUsername(username);
        redisRefreshToken.setRefresh(refresh);
        redisRefreshTokenRepository.save(redisRefreshToken);
    }


}
