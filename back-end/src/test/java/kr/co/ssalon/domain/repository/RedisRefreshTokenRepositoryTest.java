package kr.co.ssalon.domain.repository;

import kr.co.ssalon.jwt.RedisRefreshToken;
import kr.co.ssalon.jwt.RedisRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;


import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RedisRefreshTokenRepositoryTest {

    @Autowired
    RedisRefreshTokenRepository redisRefreshTokenRepository;

    String refresh = "aaaaaaaaaaaaabbbbbbbbbbbbbbbbbbcccccccccccccccccc";

    RedisRefreshToken redisRefreshToken = null;

    @BeforeEach
    public void getUsername() {
        // DB 초기화
        redisRefreshTokenRepository.deleteAll();

        // 현재 소셜 로그인한 유저(@WithMockUser) 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // refresh 토큰 객체 생성
        redisRefreshToken = new RedisRefreshToken();
        redisRefreshToken.setUsername(username);
        redisRefreshToken.setRefresh(refresh);
    }

    @Test
    @DisplayName("RedisRefreshTokenRepository.save() 테스트")
    @WithMockUser(username = "test") // 테스트 유저 인증 정보
    public void redis에refreshToken저장() {
        // given

        // when (Redis에 토큰 저장)
        redisRefreshToken = redisRefreshTokenRepository.save(redisRefreshToken);

        // then
        assertThat(redisRefreshToken.getRefresh()).isEqualTo(refresh);
    }

    @Test
    @DisplayName("RedisRefreshTokenRepository.delete() 테스트")
    @WithMockUser(username = "test") // 테스트 유저 인증 정보
    public void refreshToken삭제() {
        // given

        // refresh 토큰이 redis에 저장되어 있다고 가정
        redisRefreshTokenRepository.save(redisRefreshToken);

        // when (Redis에 있는 refresh 토큰 제거)
        redisRefreshTokenRepository.delete(redisRefreshToken);

        // then
        assertThat(redisRefreshTokenRepository.existsByRefresh(refresh)).isEqualTo(false);
    }

    @Test
    @DisplayName("RedisRefreshTokenRepository.existsByRefresh() 테스트")
    @WithMockUser(username = "test") // 테스트 유저 인증 정보
    public void refresh토큰존재여부확인() {
        // given

        // refresh 토큰이 redis에 저장되어 있다고 가정
        redisRefreshTokenRepository.save(redisRefreshToken);

        // when (refresh 토큰 존재 여부 확인)
        boolean exist = redisRefreshTokenRepository.existsByRefresh(refresh);

        // then
        assertThat(exist).isEqualTo(true);
    }

    @Test
    @DisplayName("RedisRefreshTokenRepository.findByRefresh() 테스트")
    @WithMockUser(username = "test") // 테스트 유저 인증 정보
    public void 저장되어있는refresh토큰조회() {
        // given

        // refresh 토큰이 redis에 저장되어 있다고 가정
        redisRefreshTokenRepository.save(redisRefreshToken);

        // when (refresh 토큰 조회)
        RedisRefreshToken savedRedisRefreshToken = redisRefreshTokenRepository.findByRefresh(refresh);

        // then
        assertThat(savedRedisRefreshToken.getRefresh()).isEqualTo(refresh);
    }
}
