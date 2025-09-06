package com.groupplanmanagerbe.global.notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
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
        log.info("메시지 전송 성공:{}", response);
    }
}
