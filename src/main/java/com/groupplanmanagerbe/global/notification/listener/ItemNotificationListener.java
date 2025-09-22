package com.groupplanmanagerbe.global.notification.listener;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.domain.tobuyitem.event.CreateToBuyEvent;
import com.groupplanmanagerbe.domain.todoitem.event.CreateToDoEvent;
import com.groupplanmanagerbe.global.notification.service.FcmRetryService;
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
public class ItemNotificationListener {

    private final FcmRetryService retryService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToBuy(CreateToBuyEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItem());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToDo(CreateToDoEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getItem(), event.getItemType(), event.getItem());
    }

    private void sendNotification(
            List<? extends ItemManager> managers,
            String author,
            String itemType,
            String item) throws FirebaseMessagingException {
        for (ItemManager manager : managers) {
            retryService.sendToSingleManagerWithRetry(manager, author, itemType, item);
        }
    }
}
