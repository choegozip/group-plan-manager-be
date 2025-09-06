package com.groupplanmanagerbe.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkloadIdentityCredentialWrapper {

    public static InputStream toJsonStream(String accessToken) {
        String json = "{\n" +
                "  \"type\": \"authorized_user\",\n" +
                "  \"client_id\": \"ignored\",\n" +
                "  \"client_secret\": \"ignored\",\n" +
                "  \"refresh_token\": \"" + accessToken + "\"\n" +
                "}";
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }
}
