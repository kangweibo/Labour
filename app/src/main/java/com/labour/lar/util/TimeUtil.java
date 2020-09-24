package com.labour.lar.util;

import java.util.Calendar;

public class TimeUtil {
    public static long getDatePoor(Calendar calendarStart, Calendar calendarEnd) {
        long nowDate = calendarStart.getTimeInMillis();
        long endDate = calendarEnd.getTimeInMillis();
        long nd = 1000 * 24 * 60 * 60;

        // 获得两个时间的秒时间差异
        long diff = endDate -nowDate;
        // 计算差多少天
        long day = diff / nd;
        return day;
    }
}
