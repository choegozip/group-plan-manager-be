package com.groupplanmanagerbe.global.alert.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackAlertService {

    @Value("${slack.webhook-url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;

    public void sendAlert(String title, String message) {
        try {
            Map<String, Object> payload = Map.of(
                    "text", "\uD83D\uDEA8" + title,
                    "blocks", List.of(
                            Map.of(
                                    "type", "section",
                                    "text", Map.of("type", "mrkdwn",
                                            "text", "*" + title + "*\n```" + message + "```"))
                    )
            );
            restTemplate.postForEntity(webhookUrl, payload, String.class);
        } catch (Exception e) {
            log.error("슬랙 알림 전송 실패", e);
        }
    }
}
