package com.example.h_dj.note.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by H_DJ on 2017/5/7.
 */

public class DateUtils {

    /**
     * Date 转换为String
     *
     * @param date
     * @param format
     * @return
     */
    public static String Date2String(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * String 转化为Date
     *
     * @param time
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date String2Date(String time, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(time);
    }
}
