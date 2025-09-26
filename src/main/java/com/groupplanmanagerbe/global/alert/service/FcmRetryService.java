package com.groupplanmanagerbe.global.alert.service;

import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.alert.listener.ItemManager;
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

    private static final String prefix = "alert-userId-";
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
    public void sendToEachManagerOnCreate(
            ItemManager manager,
            String title,
            String body) throws FirebaseMessagingException {
        String topic = prefix + manager.getUser().getId();

        fcmService.sendToUser(topic, title, body);
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
    public void sendToEachManagerOnUpdate(
            ItemManager manager,
            String title,
            String body) throws FirebaseMessagingException {
        String topic = prefix + manager.getUser().getId();

        fcmService.sendToUser(topic, title, body);
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
    public void sendToManagerOnStatusChange(
            Long authorId,
            String title,
            String body) throws FirebaseMessagingException {

        fcmService.sendToUser(prefix + authorId, title, body);
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
        slackAlertService.sendAlert(
                "FCM 전송 실패: " + topic,
                ex.getMessage() + "\n작성자: " + author + "\n항목: " + itemType + item);
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
        slackAlertService.sendAlert(
                "FCM 전송 실패: " + topic,
                ex.getMessage() + "\n항목: " + item + "\n담당자: " + managerNickname + "\n상태: " + status);
    }
}
