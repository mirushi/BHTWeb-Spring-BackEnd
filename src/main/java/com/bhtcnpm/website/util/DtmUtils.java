package com.bhtcnpm.website.util;

import java.time.LocalDateTime;

public class DtmUtils {
    public static LocalDateTime checkAndGetPublishDtm (LocalDateTime publishDtm) {
        LocalDateTime now = LocalDateTime.now();
        if (publishDtm == null) {
            return now;
        }
        if (publishDtm.isAfter(now)) {
            return publishDtm;
        }
        return now;
    }
}
