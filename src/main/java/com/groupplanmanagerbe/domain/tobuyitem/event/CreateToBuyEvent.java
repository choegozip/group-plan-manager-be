package com.groupplanmanagerbe.domain.tobuyitem.event;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreateToBuyEvent {
    private final String itemType = "살 것";
    private final String author;
    private final String item;
    private final List<ToBuyManager> managers;
}
