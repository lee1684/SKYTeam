package kr.co.ssalon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
<<<<<<< HEAD
                .allowedMethods("*")
                .allowCredentials(true)
=======
>>>>>>> develop
                .allowedOrigins("http://localhost:3000", "https://ssalon.co.kr");
    }
}
