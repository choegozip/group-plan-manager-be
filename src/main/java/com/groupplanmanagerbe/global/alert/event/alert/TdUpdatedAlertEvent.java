package com.groupplanmanagerbe.global.alert.event.alert;

import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public class TdUpdatedAlertEvent {
    private final String itemType = "item to do";
    private final String itemType_ko = "할 것";
    private final String author;
    private final String item;
    private final List<ToDoManager> managers;
}
