package com.groupplanmanagerbe.global.alert.listener.alert;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.alert.AlertMgrStatusChangedEvent;
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
    private final MessageResolver messageResolver;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeItemManagerStatus(AlertMgrStatusChangedEvent event) throws FirebaseMessagingException {
        sendNotification(event.authorId(), event.managerNickname(), event.item(), event.status(), event.locale());
    }

    private void sendNotification(
            Long authorId,
            String managerNickname,
            String item,
            String status,
            Locale locale) throws FirebaseMessagingException {
        String title = messageResolver.get("manager.status.changed.title", new Object[]{managerNickname, item}, locale);
        String body = messageResolver.get(ManagerStatus.of(status).getMessage(), null, locale);
        retryService.sendToManagerOnStatusChange(authorId, title, body);
    }
}
