package com.groupplanmanagerbe.global.alert.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseApp firebaseApp;

    public void sendToUser(String topic, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setTopic(topic)
                .putData("title", title)
                .putData("body", body)
                .build();

        String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
        log.info("메시지 전송 성공: {}", response);
        log.info("보낼 메시지 JSON: {}", new Gson().toJson(message));
    }

    public void sendToUser(String topic, Long spaceId, Long actorId) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setTopic(topic)
                .putData("spaceId", spaceId.toString())
                .putData("actorId", actorId.toString())
                .build();

        String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
        log.info("리프레시 시그널 전송 성공: {}", topic);
        log.info("보낼 시그널 JSON: {}", new Gson().toJson(message));
    }
}
