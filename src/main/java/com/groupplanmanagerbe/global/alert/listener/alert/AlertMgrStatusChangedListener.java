package com.groupplanmanagerbe.global.alert.listener.alert;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.alert.AlertMgrStatusChangedEvent;
import com.groupplanmanagerbe.global.alert.listener.message.AlertLocale;
import com.groupplanmanagerbe.global.alert.listener.message.AlertMessageFactory;
import com.groupplanmanagerbe.global.alert.service.FcmRetryService;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.message.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertMgrStatusChangedListener {

    private final FcmRetryService retryService;
    private final AlertMessageFactory alertMessageFactory;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeItemManagerStatus(AlertMgrStatusChangedEvent event) throws FirebaseMessagingException {
        sendNotification(event.authorId(), event.managerNickname(), event.item(), event.status());
    }

    private void sendNotification(
            Long authorId,
            String managerNickname,
            String item,
            String status) throws FirebaseMessagingException {
        AlertLocale alertLocale = alertMessageFactory.createManagerStatusChangedMessage(managerNickname, item, status);
        retryService.sendToManagerOnStatusChange(authorId, alertLocale);
    }
}
