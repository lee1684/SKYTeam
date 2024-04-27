package kr.co.ssalon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class}) // Database 없는 상황에서 에러 무시 용
public class SsalonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsalonApplication.class, args);
    }

}
