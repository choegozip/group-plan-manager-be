package com.groupplanmanagerbe.global.alert.listener.alert;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.alert.TdCreatedAlertEvent;
import com.groupplanmanagerbe.global.alert.event.alert.TbCreatedAlertEvent;
import com.groupplanmanagerbe.global.alert.listener.ItemManager;
import com.groupplanmanagerbe.global.alert.service.FcmRetryService;
import com.groupplanmanagerbe.global.message.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertCreatedListener {

    private final FcmRetryService retryService;
    private final MessageResolver messageResolver;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToBuy(TbCreatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItem(), event.getLocale());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToDo(TdCreatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getItem(), event.getItemType(), event.getItem(), event.getLocale());
    }

    private void sendNotification(
            List<? extends ItemManager> managers,
            String author,
            String itemType,
            String item,
            Locale locale) throws FirebaseMessagingException {
        String title = messageResolver.get("item.created.title", new Object[]{author, itemType}, locale);
        String body = messageResolver.get("item.created.body", new Object[]{item}, locale);
        for (ItemManager manager : managers) {
            retryService.sendToEachManagerOnCreate(manager,title, body);
        }
    }
}
