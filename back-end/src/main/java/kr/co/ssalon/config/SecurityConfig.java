package kr.co.ssalon.config;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.ssalon.filer.LoginDefaultFilter;
import kr.co.ssalon.jwt.JWTFilter;
import kr.co.ssalon.jwt.JWTUtil;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import kr.co.ssalon.oauth2.handler.CustomLogoutFilter;
import kr.co.ssalon.oauth2.handler.CustomSuccessHandler;
import kr.co.ssalon.oauth2.oauthService.CustomOAuth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomOAuth2MemberService customOAuth2MemberService;
    private final CustomSuccessHandler customSuccessHandler;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;
    private final JWTUtil jwtUtil;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        // ssalon 어플의 모든 요청 허용
                        List<String> allowedOrigins = Arrays.asList("http://localhost:3000", "https://ssalon.co.kr");
                        configuration.setAllowedOrigins(allowedOrigins);
                        // get, post, put 모든 요청 허용
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        // 클라이언트로부터 쿠키 및 토큰 전송 허용
                        configuration.setAllowCredentials(true);
                        // 어떤 헤더도 다 받을 수 있다.
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        // 요청에 대한 응답이 3600초 동안 캐시된다. 브라우저는 일정 시간 동안 동일한 요청에 대한 preflight 요청을 다시 보내지 않고 이전에 받은 응답을 사용할 수 있다.
                        configuration.setMaxAge(3600L);


                        // 클라이언트에 노출시킬 응답 헤더들
                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));


        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // JWT 필터 추가

        http
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);


        // 로그아웃 필터 추가
         http.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisRefreshTokenRepository), LogoutFilter.class);


         // accesstoken 없을 떄 작동하는 필터 추가
        http.addFilterAfter(new LoginDefaultFilter(), UsernamePasswordAuthenticationFilter.class);

        // oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2MemberService)))
                        .successHandler(customSuccessHandler)
                );


        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/tickets/**").permitAll()
                        .requestMatchers("/api/reissue").permitAll()
                        .requestMatchers("/web/ticket/**").permitAll() // 개발 완료 후 삭제 필수
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/**").permitAll());

        // 세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}
