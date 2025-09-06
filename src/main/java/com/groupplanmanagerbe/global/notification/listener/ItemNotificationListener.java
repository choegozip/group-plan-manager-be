package com.groupplanmanagerbe.global.notification.listener;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.domain.tobuyitem.event.CreateToBuyEvent;
import com.groupplanmanagerbe.domain.todoitem.event.CreateToDoEvent;
import com.groupplanmanagerbe.global.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemNotificationListener {

    private final FcmService fcmService;
    private static final String prefix = "common-" ;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToBuy(CreateToBuyEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getAuthor(), event.getItemType(), event.getItem());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateToDo(CreateToDoEvent event) throws FirebaseMessagingException {
        sendNotification(event.getManagers(), event.getItem(), event.getItemType(), event.getItem());
    }

    private void sendNotification(
            List<? extends ItemManager> managers,
            String author,
            String itemType,
            String item) throws FirebaseMessagingException
    {
        for (ItemManager manager : managers) {
            String topic = prefix + manager.getUser().getId();
            fcmService.sendToUser(
                    topic,
                    "\uD83E\uDD29" + author + "님이 새로운 " + itemType + "을 추가했어요. 확인해보세요!",
                    "추가한 항목: " + item + "✨");
        }
    }
}
