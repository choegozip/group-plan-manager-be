package com.groupplanmanagerbe.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FcmConfig {

    @Value("${firebase.service-account-path}")
    private String serviceAccountPath;

    @Value("${firebase.use-application-default:false}")
    private boolean useApplicationDefault;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions.Builder builder = FirebaseOptions.builder();

        if (useApplicationDefault) {
            builder.setCredentials(GoogleCredentials.getApplicationDefault());
        } else if (!serviceAccountPath.isEmpty()) {
        try (FileInputStream serviceAccount = new FileInputStream(serviceAccountPath)) {
            builder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
        } catch (IOException e) {
            throw new IllegalStateException("해당 경로에서 파이어베이스 어카운트 불러오기 실패: " + serviceAccountPath, e);
        }
    }
        return FirebaseApp.initializeApp(builder.build());
    }
}
