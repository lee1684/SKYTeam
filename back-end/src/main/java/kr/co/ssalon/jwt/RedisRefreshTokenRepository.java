package kr.co.ssalon.jwt;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRefreshTokenRepository extends CrudRepository<RedisRefreshToken, Long> {
    Boolean existsByRefresh(String refresh);
    RedisRefreshToken findByRefresh(String refresh);
}
