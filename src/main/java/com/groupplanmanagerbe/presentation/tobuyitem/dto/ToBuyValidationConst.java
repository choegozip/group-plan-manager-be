package com.groupplanmanagerbe.presentation.tobuyitem.dto;

public class ToBuyValidationConst {

    public static final short QUANTITY_MIN = 1;
    public static final short QUANTITY_MAX = 32767; // short의 최대값
    public static final String QUANTITY_RANGE_MESSAGE = "quantity.range";
    public static final String QUANTITY_NOT_NULL_MESSAGE = "quantity.notnull";

    // dueDate
    // 허용하는 날짜 형식: yyyy-MM-dd, yyyy.MM.dd, yyyy/MM/dd, yyyyMMdd
    public static final String DUE_DATE_REG = "^(\\d{4}[-./,]?(\\d{2})[-./,]?(\\d{2}))$";
    public static final String DUE_DATE_INVALID_MESSAGE = "invalid.date.format";

    public static final String TITLE_BLANK_MESSAGE = "title.blank";

    public static final String URGENCY_BLANK_MESSAGE = "urgency.blank";

    public static final String MANAGER_IDS_BLANK_MESSAGE = "managerIds.blank";
}
