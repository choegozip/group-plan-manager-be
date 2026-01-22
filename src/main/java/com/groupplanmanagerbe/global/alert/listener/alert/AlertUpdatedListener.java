package com.groupplanmanagerbe.global.alert.listener.alert;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.alert.TbUpdatedAlertEvent;
import com.groupplanmanagerbe.global.alert.event.alert.TdUpdatedAlertEvent;
import com.groupplanmanagerbe.global.alert.listener.ItemManager;
import com.groupplanmanagerbe.global.alert.listener.message.AlertLocale;
import com.groupplanmanagerbe.global.alert.listener.message.AlertMessageFactory;
import com.groupplanmanagerbe.global.alert.service.FcmRetryService;
import com.groupplanmanagerbe.global.message.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertUpdatedListener {

    private final FcmRetryService retryService;
    private final AlertMessageFactory alertMessageFactory;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateToBuy(TbUpdatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItemType_ko(), event.getItem());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateToDo(TdUpdatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItemType_ko(), event.getItem());
    }

    private void sendNotification(
            List<? extends ItemManager> managers,
            String author,
            String itemType,
            String itemType_ko,
            String item) throws FirebaseMessagingException {
        AlertLocale alertLocale = alertMessageFactory.createItemUpdatedMessage(author, itemType, itemType_ko, item);

        for (ItemManager manager : managers) {
            retryService.sendToEachManagerOnUpdate(manager, alertLocale);
        }
    }
}
