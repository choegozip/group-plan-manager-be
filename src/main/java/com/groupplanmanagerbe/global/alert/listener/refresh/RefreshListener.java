package com.groupplanmanagerbe.global.alert.listener.refresh;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.refresh.RefreshEvent;
import com.groupplanmanagerbe.global.alert.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RefreshListener {

    private final FcmService fcmService;
    private static final String prefix = "refresh-spaceId-";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRefreshEvent(RefreshEvent event) throws FirebaseMessagingException {
        String topic =  prefix + event.spaceId();
        fcmService.sendToUser(topic, event.spaceId());
    }
}
