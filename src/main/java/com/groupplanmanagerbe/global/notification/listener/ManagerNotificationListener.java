package com.groupplanmanagerbe.global.notification.listener;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.domain.tobuyitem.event.ChangeToBuyMgrStatusEvent;
import com.groupplanmanagerbe.domain.todoitem.event.ChangeToDoMgrStatusEvent;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ManagerNotificationListener {

    private final FcmService fcmService;
    private static final String prefix = "common-" ;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeToBuyManagerStatus(ChangeToBuyMgrStatusEvent event) throws FirebaseMessagingException {
        sendNotification(event.authorId(), event.managerNickname(), event.item(), event.status());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChangeToDoManagerStatus(ChangeToDoMgrStatusEvent event) throws FirebaseMessagingException {
        sendNotification(event.authorId(), event.managerNickname(), event.item(), event.status());
    }

    private void sendNotification(
            Long authorId,
            String managerNickname,
            String item,
            String status) throws FirebaseMessagingException {
        fcmService.sendToUser(
                prefix + authorId,
                managerNickname + "님이 " + item + "요청에 " + "응답했어요!",
                ManagerStatus.of(status).getMessage());
    }
}
