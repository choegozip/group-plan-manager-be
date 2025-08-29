package com.groupplanmanagerbe.global.util;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.InvalidException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static final DateTimeFormatter[] FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("yyyy,MM,dd")
    };

    public static LocalDateTime parseDateTime(String date) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                return localDate.atStartOfDay();
            } catch (DateTimeParseException e) {
                // 포멧 불일치시 다음 패턴 실행
            }
        }
        throw new InvalidException(ApiErrorCode.INVALID_DATE_FORMAT);
    }

    public static boolean isAfterOrEqualsToday(LocalDateTime dateTime) {
        LocalDate today = LocalDate.now();
        return !dateTime.toLocalDate().isBefore(today);
    }

    public static LocalDateTime isValidFutureDate(String dateStr) {
        LocalDateTime dateTime = parseDateTime(dateStr);
        if (isAfterOrEqualsToday(dateTime)) {
            return dateTime;
        }
        throw new InvalidException(ApiErrorCode.INVALID_DATE);
    }
}
