package kr.co.ssalon.web.controller.oauth2;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshToken;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@Tag(name = "토큰")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;


    
    @Operation(summary = "Access Token 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access Token 재발급 성공"),
    })
    @Parameter(name = "Refresh", description = "Refresh Header에 refresh token 추가", in = ParameterIn.HEADER, required = true)
    @PostMapping("/api/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("refresh 토큰 검증 및 access 토큰 재발급");
        String refresh = null;
        String refreshCookie = null;

        refresh = request.getHeader("Refresh");

        // 쿠키 토큰 사용
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refreshCookie = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null && refreshCookie == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }


        try {
            if (refresh != null) {
                jwtUtil.isExpired(refresh);
            } else {
                jwtUtil.isExpired(refreshCookie);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }


        String category = refresh != null ? jwtUtil.getCategory(refresh) : jwtUtil.getCategory(refreshCookie);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invaild refresh token", HttpStatus.BAD_REQUEST);
        }


        Boolean isExist = redisRefreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);


        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);


        RedisRefreshToken deleteRefresh = redisRefreshTokenRepository.findByRefresh(refresh);
        redisRefreshTokenRepository.delete(deleteRefresh);

        addRedisRefreshEntity(username, newRefresh);

        // response에 쿠키 추가
        Cookie newAccessCookie = createCookieAccess("access", newAccess);
        Cookie newRefreshCookie = createCookieRefresh("refresh", newRefresh);

        response.addCookie(newAccessCookie);
        response.addCookie(newRefreshCookie);

        // JSON 객체 생성
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("access", newAccess);
        jsonResponse.put("refresh", newRefresh);

        // response body의 MIME 타입 설정
        response.setContentType("application/json");

        // JSON response body 전송
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Cookie createCookieRefresh(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400000);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private Cookie createCookieAccess(String key, String value) {

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
