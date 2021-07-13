package com.bhtcnpm.website.constant.business;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class GenericBusinessConstant {
    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";

    public static final LocalDateTime WEB_START_TIME = LocalDateTime.of(2015, 11, 2, 0, 0);
    public static final long WEB_START_TIME_EPOCH = WEB_START_TIME.toEpochSecond(ZoneOffset.UTC);

    public static final int URL_MAX_LENGTH = 2048;
}
