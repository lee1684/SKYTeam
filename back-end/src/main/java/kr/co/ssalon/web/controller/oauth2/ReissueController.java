package kr.co.ssalon.web.controller.oauth2;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshToken;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("refresh 토큰 검증 및 access 토큰 재발급");
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }


        try {
            jwtUtil.isExpired(refresh);

        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }


        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invaild refresh token", HttpStatus.BAD_REQUEST);
        }


        Boolean isExist = redisRefreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);


        String newAccess = jwtUtil.createJwt("Authorization", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);


        RedisRefreshToken deleteRefresh = redisRefreshTokenRepository.findByRefresh(refresh);
        redisRefreshTokenRepository.delete(deleteRefresh);

        addRedisRefreshEntity(username, newRefresh);

        response.addCookie(createCookieAccess("Authorization", newAccess));
        response.addCookie(createCookie("refresh", newRefresh));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private Cookie createCookieAccess(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(600);
        //cookie.setSecure(true);
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