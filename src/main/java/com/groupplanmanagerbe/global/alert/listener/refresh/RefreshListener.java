package com.groupplanmanagerbe.global.alert.listener.refresh;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groupplanmanagerbe.global.alert.event.refresh.RefreshEvent;
import com.groupplanmanagerbe.global.alert.service.FcmService;
import com.groupplanmanagerbe.global.common.enums.RefreshType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RefreshListener {

    private final FcmService fcmService;
    private static final Map<RefreshType, String> prefixMap = Map.of(
            RefreshType.TODO, "refresh-todo-",
            RefreshType.TOBUY, "refresh-tobuy-"
    );

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRefreshEvent(RefreshEvent event) throws FirebaseMessagingException {
        String topic = prefixMap.get(event.refreshType()) + event.spaceId();
        fcmService.sendToUser(topic, event.spaceId(), event.actorId());
    }
}
