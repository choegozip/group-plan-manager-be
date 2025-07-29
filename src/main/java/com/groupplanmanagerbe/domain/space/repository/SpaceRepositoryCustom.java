package com.groupplanmanagerbe.domain.space.repository;

import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.presentation.space.dto.response.SpacesRes;

import java.util.List;

public interface SpaceRepositoryCustom {
    List<SpacesRes> findSpacesWithCursor(CursorPageRequest request, Long userId);
}
