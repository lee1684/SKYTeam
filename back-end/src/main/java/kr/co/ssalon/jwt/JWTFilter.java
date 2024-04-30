package kr.co.ssalon.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.Oauth2MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {


    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JWTFilter");
        String accessToken = null;

        // 'Authorization' 헤더 추출
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Bearer 토큰에서 실제 토큰 값만 추출
            accessToken = authorizationHeader.substring(7);
        }

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return ;
        }

        try {
            jwtUtil.isExpired(accessToken);

        } catch (ExpiredJwtException e) {

            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ;

        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ;
        }

        // 스프링 시큐리티 세션에 유저 정보 강제 저장
        String token = accessToken;
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        Oauth2MemberDto oauth2MemberDto = Oauth2MemberDto.builder().username(username).role(role).build();
        CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(oauth2MemberDto);
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(customOAuth2Member, null, customOAuth2Member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
