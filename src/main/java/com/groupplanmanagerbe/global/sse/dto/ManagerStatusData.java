package com.groupplanmanagerbe.global.sse.dto;

import com.groupplanmanagerbe.domain.tobuyitem.entity.ToBuyManager;
import com.groupplanmanagerbe.domain.todoitem.entity.ToDoManager;
import com.groupplanmanagerbe.domain.user.entity.User;
import com.groupplanmanagerbe.global.common.enums.ManagerStatus;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record ManagerStatusData(
        Long id,
        List<ManagerInfoForList> managers
) {
    public static ManagerStatusData of(Long toBuyId, ToDoManager manager){
        List<ManagerInfoForList> managers = new ArrayList<>();
        managers.add(ManagerInfoForList.of(manager));
        return ManagerStatusData.builder()
                .id(toBuyId)
                .managers(managers)
                .build();
    }

    public static ManagerStatusData of(Long toBuyId, ToBuyManager manager){
        List<ManagerInfoForList> managers = new ArrayList<>();
        managers.add(ManagerInfoForList.of(manager));
        return ManagerStatusData.builder()
                .id(toBuyId)
                .managers(managers)
                .build();
    }

    @Builder
    public record ManagerInfoForList(
            Long id,
            ManagerStatus status
    ) {
        public static ManagerInfoForList of(ToBuyManager manager) {
            User user = manager.getUser();
            return ManagerInfoForList.builder()
                    .id(user.getId())
                    .status(manager.getStatus())
                    .build();
        }

        public static ManagerInfoForList of(ToDoManager manager) {
            User user = manager.getUser();
            return ManagerInfoForList.builder()
                    .id(user.getId())
                    .status(manager.getStatus())
                    .build();
        }
    }
}
