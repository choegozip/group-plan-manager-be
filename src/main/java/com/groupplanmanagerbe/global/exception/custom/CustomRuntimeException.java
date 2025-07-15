package com.groupplanmanagerbe.global.exception.custom;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomRuntimeException extends RuntimeException {
   private final ApiErrorCode errorCode;
}
