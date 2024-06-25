package kr.co.ssalon.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCredentialsConfig {

    @Value("${google.cloud.credentials.location}")
    private Resource credentialsLocation;

    @Bean
    public Translate translate() throws IOException {
        try (InputStream inputStream = credentialsLocation.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }
}
