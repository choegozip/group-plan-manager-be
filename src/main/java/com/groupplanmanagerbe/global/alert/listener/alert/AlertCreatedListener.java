package com.groupplanmanagerbe.global.alert.listener.alert;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.alert.TdCreatedAlertEvent;
import com.groupplanmanagerbe.global.alert.event.alert.TbCreatedAlertEvent;
import com.groupplanmanagerbe.global.alert.listener.ItemManager;
import com.groupplanmanagerbe.global.alert.listener.message.AlertLocale;
import com.groupplanmanagerbe.global.alert.listener.message.AlertMessageFactory;
import com.groupplanmanagerbe.global.alert.service.FcmRetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertCreatedListener {

    private final FcmRetryService retryService;
    private final AlertMessageFactory alertMessageFactory;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToBuy(TbCreatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItemType_ko(), event.getItem());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToDo(TdCreatedAlertEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getItem(), event.getItemType(), event.getItemType_ko(), event.getItem());
    }

    private void sendNotification(
            List<? extends ItemManager> managers,
            String author,
            String itemType,
            String itemType_ko,
            String item) throws FirebaseMessagingException {
        AlertLocale alertLocale = alertMessageFactory.createItemCreatedMessage(author, itemType, itemType_ko, item);
        for (ItemManager manager : managers) {
            retryService.sendToEachManagerOnCreate(manager, alertLocale);
        }
    }
}
