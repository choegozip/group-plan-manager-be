package com.groupplanmanagerbe.global.notification.service;

import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.notification.listener.ItemManager;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

@Service
@RequiredArgsConstructor
public class FcmRetryService {

    private static final String prefix = "common-";
    private final SlackAlertService slackAlertService;
    private final FcmService fcmService;

    @Retryable(
            retryFor = {
                    FirebaseException.class,
                    FirebaseMessagingException.class,
                    IOException.class,              // 네트워크 오류
                    ConnectException.class,         // 연결 오류
                    SocketTimeoutException.class    // 타임아웃
            },
            noRetryFor = {
                    IllegalArgumentException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendToSingleManagerWithRetry(
            ItemManager manager,
            String author,
            String itemType,
            String item) throws FirebaseMessagingException {
        String topic = prefix + manager.getUser().getId();
        fcmService.sendToUser(topic,
                "\uD83E\uDD29" + author + "님이 새로운 " + itemType + "을 추가했어요. 확인해보세요!",
                "추가한 항목: " + item + "✨");
    }

    @Retryable(
            retryFor = {
                    FirebaseException.class,
                    FirebaseMessagingException.class,
                    IOException.class,              // 네트워크 오류
                    ConnectException.class,         // 연결 오류
                    SocketTimeoutException.class    // 타임아웃
            },
            noRetryFor = {
                    IllegalArgumentException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendManagerStatusWithRetry(
            Long authorId,
            String managerNickname,
            String item, String status) throws FirebaseMessagingException
    {
        fcmService.sendToUser(
                prefix + authorId,
                managerNickname + "님이 '" + item + "' 요청에 " + "응답했어요!",
                ManagerStatus.of(status).getMessage());
    }

    @Recover
    public void recoverFromTopicFailure(
            Exception ex,
            ItemManager manager,
            String author,
            String itemType,
            String item
    ) {
        String topic = prefix + manager.getUser().getId();
        String title = "\uD83E\uDD29" + author + "님이 새로운 " + itemType + "을 추가했어요.";
        String body = "추가한 항목: " + item + "✨";

        slackAlertService.sendAlert("FCM 전송 실패: " + topic, ex.getMessage() + "\n제목: " + title + "\n본문: " + body);
    }

    @Recover
    public void recoverFromTopicFailure(
            Exception ex,
            Long authorId,
            String managerNickname,
            String item,
            String status
    ) {
        String topic = prefix + authorId;
        String title = managerNickname + "님이 '" + item + "' 요청에 응답했어요!";
        String body = ManagerStatus.of(status).getMessage();

        slackAlertService.sendAlert("FCM 전송 실패: " + topic, ex.getMessage() + "\n제목: " + title + "\n본문: " + body);
    }
}
