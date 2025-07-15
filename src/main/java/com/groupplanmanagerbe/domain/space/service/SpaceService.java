package com.groupplanmanagerbe.domain.space.service;

import com.groupplanmanagerbe.domain.space.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpaceService {
    private final SpaceRepository spaceRepository;
}
