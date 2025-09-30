package com.groupplanmanagerbe.global.alert.listener.message;

import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.message.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AlertMessageFactory {

    private final MessageResolver messageResolver;

    public AlertLocale createItemCreatedMessage(String author, String itemType, String itemType_ko, String item) {
        String titleKey = "item.created.title";
        String bodyKey = "item.created.body";

        return AlertLocale.of(
                messageResolver.get(titleKey, new Object[]{author, itemType}, Locale.getDefault()),
                messageResolver.get(bodyKey, new Object[]{item}, Locale.getDefault()),
                messageResolver.get(titleKey, new Object[]{author, itemType_ko}, Locale.KOREAN),
                messageResolver.get(bodyKey, new Object[]{item}, Locale.KOREAN));
    }

    public AlertLocale createItemUpdatedMessage(String author, String itemType, String itemType_ko, String item) {
        String titleKey = "item.updated.title";
        String bodyKey = "item.updated.body";

        return AlertLocale.of(
                messageResolver.get(titleKey, new Object[]{author, itemType}, Locale.getDefault()),
                messageResolver.get(bodyKey, new Object[]{item}, Locale.getDefault()),
                messageResolver.get(titleKey, new Object[]{author, itemType_ko}, Locale.KOREAN),
                messageResolver.get(bodyKey, new Object[]{item}, Locale.KOREAN));
    }
    public AlertLocale createManagerStatusChangedMessage(String managerNickname, String item, String status) {
        String titleKey = "manager.status.changed.title";

        return AlertLocale.of(
                messageResolver.get(titleKey, new Object[]{managerNickname, item}, Locale.getDefault()),
                messageResolver.get(ManagerStatus.of(status).getMessage(), null, Locale.getDefault()),
                messageResolver.get(titleKey, new Object[]{managerNickname, item}, Locale.KOREAN),
                messageResolver.get(ManagerStatus.of(status).getMessage(), null, Locale.KOREAN));
    }
}
