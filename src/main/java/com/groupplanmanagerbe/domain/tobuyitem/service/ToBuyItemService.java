package com.groupplanmanagerbe.domain.tobuyitem.service;

import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToBuyItemService {
    private final ToBuyItemRepository toBuyItemRepository;
}
