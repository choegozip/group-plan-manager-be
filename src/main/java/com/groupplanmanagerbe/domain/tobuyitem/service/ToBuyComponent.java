package com.groupplanmanagerbe.domain.tobuyitem.service;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToBuyComponent {

    private final ToBuyItemRepository toBuyItemRepository;

    public ToBuyItem getReferenceById(Long toBuyId) {
        return toBuyItemRepository.getReferenceById(toBuyId);
    }
}
