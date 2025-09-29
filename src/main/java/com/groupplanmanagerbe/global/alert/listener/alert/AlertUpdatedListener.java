package com.groupplanmanagerbe.global.alert.listener.alert;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.alert.TbUpdatedAlertEvent;
import com.groupplanmanagerbe.global.alert.event.alert.TdUpdatedAlertEvent;
import com.groupplanmanagerbe.global.alert.listener.ItemManager;
import com.groupplanmanagerbe.global.alert.service.FcmRetryService;
import com.groupplanmanagerbe.global.message.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AlertUpdatedListener {

    private final FcmRetryService retryService;
    private final MessageResolver messageResolver;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateToBuy(TbUpdatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItem(), event.getLocale());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateToDo(TdUpdatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItem(), event.getLocale());
    }

    private void sendNotification(
            List<? extends ItemManager> managers,
            String author,
            String itemType,
            String item,
            Locale locale) throws FirebaseMessagingException {
        String title = messageResolver.get(
                "item.updated.title", new Object[]{author, itemType}, locale);
        String body = messageResolver.get("item.updated.body", new Object[]{item}, locale);

        for (ItemManager manager : managers) {
            retryService.sendToEachManagerOnUpdate(manager, title, body);
        }
    }
}
