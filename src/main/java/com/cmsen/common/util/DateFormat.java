/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class DateFormat {

    public static String format(String dateString, String pattern, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatePatternType.FORMAT_MM_yy);
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date date = dateFormat.parse(dateString, new ParsePosition(0));
        return format(date, pattern, locale);
    }

    public static String format(Date date, String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    public static String format(Date date, String pattern) {
        return format(date, pattern, Locale.CHINESE);
    }

    public static Date parse(String dateString) {
        return DatePatternType.parseStringToDate(dateString);
    }

    public static String parse(String dateString, String pattern) {
        Date date = DatePatternType.parseStringToDate(dateString);
        return new SimpleDateFormat(pattern, Locale.CHINESE).format(date);
    }

    /**
     * Date time pattern type for format
     */
    public static class DatePatternType {
        public final static String YEAR = "yyyy";
        public final static String YEAR_SIMPLE = "yy";
        public final static String MONTH = "MM";
        public final static String MONTH_SIMPLE = "M";
        public final static String DATE = "dd";
        public final static String DATE_SIMPLE = "d";
        public final static String HOUR = "HH";
        public final static String HOUR_SIMPLE = "H";
        public final static String MINUTE = "mm";
        public final static String MINUTE_SIMPLE = "m";
        public final static String SECOND = "ss";
        public final static String SECOND_SIMPLE = "s";

        public final static String FORMAT_yyyy = "yyyy";
        public final static String FORMAT_yyyyMM = "yyyyMM";
        public final static String FORMAT_yyyy_MM = "yyyy-MM";
        public final static String FORMAT_yy_MM = "yy-MM";
        public final static String FORMAT_yyMM = "yyMM";
        public final static String FORMAT_yyMMdd = "yyMMdd";
        public final static String FORMAT_yy_MM_dd = "yy-MM-dd";
        public final static String FORMAT_yyyyMMdd = "yyyyMMdd";
        public final static String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
        public final static String FORMAT_yyyy年MM月dd日 = "yyyy年MM月dd日";
        public final static String FORMAT_yyyyMMddHHmm = "yyyyMMddHHmm";
        public final static String FORMAT_yyyyMMdd_HH_mm = "yyyyMMdd HH:mm";
        public final static String FORMAT_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
        public final static String FORMAT_yyyyMMddHHmmss = "yyyyMMddHHmmss";
        public final static String FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
        public final static String FORMAT_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
        public final static String FORMAT_yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ssSSS";
        public final static String FORMAT_MM_yy = "MM/yy";
        public final static String FORMAT_MM_dd = "MM-dd";
        public final static String FORMAT_MMM = "MMM";
        public final static String FORMAT_MMMM = "MMMM";
        public final static String FORMAT_MM_dd_HH_mm = "MM-dd HH:mm";

        /**
         * Date string convert Date object
         * ,which is interpreted as if by the {@link DateFormat#parse parse}
         *
         * @param dateString date time String
         * @return the parsed date String to {@link Date} object
         * @see SimpleDateFormat SimpleDateFormat(String pattern)
         */
        public static Date parseStringToDate(String dateString) {
            if (dateString == null || dateString.trim().length() <= 0) {
                return null;
            }
            dateString = dateString.replaceAll("[\\/|\\.]+", "-");
            List<String> formatList = new ArrayList<String>();
            formatList.add(FORMAT_yyyy);
            formatList.add(FORMAT_yyyyMM);
            formatList.add(FORMAT_yyyy_MM);
            formatList.add(FORMAT_yy_MM);
            formatList.add(FORMAT_yyMM);
            formatList.add(FORMAT_yyMMdd);
            formatList.add(FORMAT_yy_MM_dd);
            formatList.add(FORMAT_yyyyMMdd);
            formatList.add(FORMAT_yyyy_MM_dd);
            formatList.add(FORMAT_yyyy年MM月dd日);
            formatList.add(FORMAT_yyyyMMddHHmm);
            formatList.add(FORMAT_yyyyMMdd_HH_mm);
            formatList.add(FORMAT_yyyy_MM_dd_HH_mm);
            formatList.add(FORMAT_yyyyMMddHHmmss);
            formatList.add(FORMAT_yyyy_MM_dd_HH_mm_ss);
            formatList.add(FORMAT_yyyyMMddHHmmssSSS);
            formatList.add(FORMAT_yyyy_MM_dd_HH_mm_ss_SSS);
            formatList.add(FORMAT_MM_dd);
            formatList.add(FORMAT_MM_dd_HH_mm);

            Date date = null;
            for (String pattern : formatList) {
                if (dateString.indexOf("-") > 0 && !pattern.contains("-")) {
                    continue;
                }
                if (!dateString.contains("-") && pattern.indexOf("-") > 0) {
                    continue;
                }
                if (dateString.length() > pattern.length()) {
                    continue;
                }
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setTimeZone(TimeZone.getDefault());
                    date = dateFormat.parse(dateString, new ParsePosition(0));
                } catch (Exception e) {
                }
                if (date != null) {
                    break;
                }
            }
            return date;
        }
    }
}
