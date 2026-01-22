package com.groupplanmanagerbe.presentation.user.dto;

public class UserValidationConst {

    public static final String EMAIL_REG = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]$";
    public static final String PASSWORD_REG = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+])[a-zA-Z\\d!@#$%^&*()\\-_=+.]*$";
    public static final int PASSWORD_MIN = 8;
    public static final int NICKNAME_MAX = 15;
    public static final int NICKNAME_MIN = 2;

    public static final String EMAIL_BLANK_MESSAGE = "user.email.blank";
    public static final String INVALID_EMAIL_MESSAGE = "user.email.invalid";

    public static final String PASSWORD_BLANK_MESSAGE = "user.password.blank";
    public static final String INVALID_PASSWORD_MESSAGE = "user.password.invalid";
    public static final String PASSWORD_MIN_MESSAGE = "user.password.min";

    public static final String NICKNAME_BLANK_MESSAGE = "user.nickname.blank";
    public static final String NICKNAME_RANGE_MESSAGE = "user.nickname.range";

    public static final String CODE_BLANK_MESSAGE = "mail.code.blank";

    public static final String USER_ROLE_MESSAGE = "user.role.required";
}
