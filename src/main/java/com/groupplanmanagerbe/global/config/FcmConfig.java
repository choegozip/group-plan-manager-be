package com.groupplanmanagerbe.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FcmConfig {

    @Value("${firebase.service-account-json}")
    private String serviceAccountJson;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions.Builder builder = FirebaseOptions.builder();

        byte[] decodedKey = Base64.getDecoder().decode(serviceAccountJson);
        InputStream serviceAccount = new ByteArrayInputStream(decodedKey);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        return FirebaseApp.initializeApp(options);
    }
}
