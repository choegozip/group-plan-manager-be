package com.groupplanmanagerbe.domain.tobuyitem.service;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyItem;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyItemRepository;
import com.groupplanmanagerbe.domain.tobuyitem.repository.ToBuyManagerRepository;
import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.common.response.page.CursorPageRequest;
import com.groupplanmanagerbe.global.exception.custom.NotFoundException;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.ToBuyListProjection;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.ParamReq;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ToBuyComponent {

    private final ToBuyItemRepository toBuyItemRepository;
    private final ToBuyManagerRepository toBuyManagerRepository;

    public int countBySpaceId(Long spaceId) {
        return toBuyItemRepository.countBySpaceId(spaceId);
    }

    public ToBuyItem getReferenceById(Long toBuyId) {
        return toBuyItemRepository.getReferenceById(toBuyId);
    }

    public ToBuyItem getByIdAndSpaceId(Long toBuyId, Long spaceId) {
        return toBuyItemRepository.findByIdAndSpaceIdWithUser(toBuyId, spaceId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
    }

    public ToBuyItem getByIdAndSpaceIdAndUserId(Long toBuyId, Long spaceId, Long userId) {
        return toBuyItemRepository.findByIdAndSpaceIdAndUserId(toBuyId, spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
    }

    public ToBuyItem getByIdAndSpaceIdAndUserIdWithSpaceAndUser(Long toBuyId, Long spaceId, Long userId) {
        return toBuyItemRepository.findByIdAndSpaceIdAndUserIdWithSpaceAndUser(toBuyId, spaceId, userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.TO_BUY_NOT_FOUND));
    }

    public List<ToBuyListProjection> getToBuyItemsNative(
            Long spaceId, Long userId, ParamReq params, CursorPageRequest request) {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        return toBuyItemRepository.findToBuyItemsNative(
                spaceId, userId, params.managerId(), params.urgency(), request.cursor(),
                request.direction(), request.size(), params.includeExpired(), startOfDay);
    }

    public ToBuyManager getByIdAndSpaceIdAndToBuyIdWithToBuy(Long managerId, Long spaceId, Long toBuyId) {
        return toBuyManagerRepository.findByIdAndSpaceIdAndToBuyIdWithToBuy(managerId, spaceId, toBuyId)
                .orElseThrow(() -> new NotFoundException(ApiErrorCode.MANAGER_NOT_FOUND));
    }

    public List<ToBuyManager> getByToBuyItemIdsWithUser(List<Long> toBuyIds, Long userId) {
        return toBuyManagerRepository.findByToBuyItemIdsWithUser(toBuyIds, userId);
    }

    public List<ToBuyManager> getAllByToBuyItemId(Long toBuyId, Long userId) {
        return toBuyManagerRepository.findAllByToBuyItemId(toBuyId, userId);
    }
}
