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
import java.io.PrintWriter;

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
        if (!requestURI.matches("^\\/api\\/auth\\/logout$")) {
            chain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();

        if (!requestMethod.equals("DELETE")) {
            chain.doFilter(request, response);
            return;
        }

        String refresh = null;

        refresh = request.getHeader("Refresh");
        if (refresh == null) {
            sendResponse(response, "refresh token is null", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            sendResponse(response, "refresh token is expired", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            sendResponse(response, "not a refresh token", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isExist = redisRefreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {
            sendResponse(response, "refresh token does not exist", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 진행
        RedisRefreshToken deleteRefresh = redisRefreshTokenRepository.findByRefresh(refresh);
        redisRefreshTokenRepository.delete(deleteRefresh);

        // 'access' 및 'refresh' 쿠키 제거
        clearCookie(response, "access");
        clearCookie(response, "refresh");

        response.sendRedirect("https://ssalon.co.kr");
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 만료
        response.addCookie(cookie);
    }

    private void sendResponse(HttpServletResponse response, String message, int status) {
        log.info(message);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(message);
            writer.flush();
        } catch (IOException e) {
            log.error("Error writing to response", e);
        }
        response.setStatus(status);
    }
}
