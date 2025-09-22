package com.groupplanmanagerbe.global.notification.listener;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.domain.tobuyitem.event.ChangeToBuyMgrStatusEvent;
import com.groupplanmanagerbe.domain.todoitem.event.ChangeToDoMgrStatusEvent;
import com.groupplanmanagerbe.global.notification.service.FcmRetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagerNotificationListener {

    private final FcmRetryService retryService;
    private static final String prefix = "common-";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeToBuyManagerStatus(ChangeToBuyMgrStatusEvent event) throws FirebaseMessagingException {
        sendNotification(event.authorId(), event.managerNickname(), event.item(), event.status());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeToDoManagerStatus(ChangeToDoMgrStatusEvent event) throws FirebaseMessagingException {
        sendNotification(event.authorId(), event.managerNickname(), event.item(), event.status());
    }

    private void sendNotification(
            Long authorId,
            String managerNickname,
            String item,
            String status) throws FirebaseMessagingException {
        retryService.sendManagerStatusWithRetry(authorId, managerNickname, item, status);
    }
}
