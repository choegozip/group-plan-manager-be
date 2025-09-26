package com.groupplanmanagerbe.global.alert.event.alert;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public class TbCreatedAlertEvent {
    private final String itemType = "살 것";
    private final String author;
    private final String item;
    private final List<ToBuyManager> managers;
    private final Locale locale;
}
