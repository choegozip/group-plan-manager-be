package com.groupplanmanagerbe.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.groupplanmanagerbe.global.util.WorkloadIdentityCredentialWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FcmConfig {

    @Value("${firebase.service-account-path:}")
    private String serviceAccountPath;

    @Value("${firebase.use-application-default:false}")
    private boolean useApplicationDefault;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions.Builder builder = FirebaseOptions.builder();

        if (useApplicationDefault) {
            String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
            if (credentialsJson != null && !credentialsJson.isEmpty()) {
                InputStream serviceAccount = WorkloadIdentityCredentialWrapper.toJsonStream(credentialsJson);
                builder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
            } else {
                builder.setCredentials(GoogleCredentials.getApplicationDefault());
            }
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
