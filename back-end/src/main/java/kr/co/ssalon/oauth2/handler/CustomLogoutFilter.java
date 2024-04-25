package kr.co.ssalon.oauth2.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshToken;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("logoutFilter");
        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/auth\\/logout$")) {
            chain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();

        if (!requestMethod.equals("DELETE")) {
            chain.doFilter(request, response);
            return;
        }
        log.info("logoutFilter check");
        String refresh = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            jwtUtil.isExpired(refresh);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Boolean isExist = redisRefreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        //***** 로그아웃 진행 *****
        RedisRefreshToken deleteRefresh = redisRefreshTokenRepository.findByRefresh(refresh);
        redisRefreshTokenRepository.delete(deleteRefresh);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Cookie createLogoutRefreshCookie() {
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    private Cookie createLogoutAccessCookie() {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
