package com.groupplanmanagerbe.global.common.enums;

import lombok.Getter;

@Getter
public enum ManagerStatus {
    REQUESTED,  // 도움 요청
    ACCEPTED,   // 수락
    REJECTED,   // 거절
    COMPLETED   // 완료
}
