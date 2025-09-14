package com.groupplanmanagerbe.global.sse.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import com.groupplanmanagerbe.global.common.enums.Urgency;
import com.groupplanmanagerbe.global.util.DateUtil;
import com.groupplanmanagerbe.presentation.tobuyitem.dto.request.UpdateToBuyReq;
import com.groupplanmanagerbe.presentation.todoitem.dto.request.UpdateToDoReq;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ToBuyData(
        Long id,
        String detail,
        String title,
        Short quantity,
        LocalDateTime dueDate,
        Urgency urgency,
        Boolean hasMemo,
        Boolean hasLink,
        List<ManagerInfoForList> managers
) {
    public static ToBuyData of(Long toBuyId, UpdateToBuyReq request, List<ToBuyManager> managers) {
        List<ManagerInfoForList> managerInfos = managers.stream()
                .map(ManagerInfoForList::of)
                .toList();

        return ToBuyData.builder()
                .id(toBuyId)
                .title(request.title())
                .quantity(request.quantity())
                .dueDate(request.dueDate() != null && request.dueDate().isEmpty() ? DateUtil.isValidFutureDate(request.dueDate()) : null)
                .urgency(Urgency.of(request.urgency()))
                .hasMemo((request.memo() != null && !request.memo().isEmpty()) ? Boolean.TRUE : null)
                .hasLink((request.referenceUrl() != null && !request.referenceUrl().isEmpty()) ? Boolean.TRUE : null)
                .managers(managerInfos.isEmpty() ? null : managerInfos)
                .build();
    }

    public static ToBuyData of(Long toBuyId, UpdateToDoReq request, List<ToDoManager> managers) {
        List<ManagerInfoForList> managerInfos = managers.stream()
                .map(ManagerInfoForList::of)
                .toList();

        return ToBuyData.builder()
                .id(toBuyId)
                .title(request.title())
                .detail(request.detail())
                .dueDate(request.dueDate() != null && request.dueDate().isEmpty() ? DateUtil.isValidFutureDate(request.dueDate()) : null)
                .urgency(Urgency.of(request.urgency()))
                .hasLink((request.referenceUrl() != null && !request.referenceUrl().isEmpty()) ? Boolean.TRUE : null)
                .managers(managerInfos.isEmpty() ? null : managerInfos)
                .build();
    }

    @Builder
    public record ManagerInfoForList(
            Long id,
            String nickname,
            String profileImageKey,
            ManagerStatus status
    ) {
        public static ManagerInfoForList of(ToBuyManager manager) {
            User user = manager.getUser();
            return ManagerInfoForList.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
                    .status(manager.getStatus())
                    .build();
        }

        public static ManagerInfoForList of(ToDoManager manager) {
            User user = manager.getUser();
            return ManagerInfoForList.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImageKey(user.getProfileImageKey())
                    .status(manager.getStatus())
                    .build();
        }
    }
}
