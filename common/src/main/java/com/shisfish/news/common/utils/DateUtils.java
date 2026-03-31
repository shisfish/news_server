package com.shisfish.news.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * @author shisfish
 * @date 2023/8/16
 * @Description 时间工具
 */
public class DateUtils {

    public final static long ONE_DAY_SECONDS = 86400;

    public final static String SHORT_FORMAT_REVERSE = "ddMMyy";
    public final static String SHORT_FORMAT = "yyyyMMdd";
    public final static String LONG_FORMAT = "yyyyMMddHHmmss";
    public final static String WEB_FORMAT = "yyyy-MM-dd";
    public final static String MM_AND_DATE = "MM-dd";
    public final static String WEB_FORMAT_LINE = "yyyy/MM/dd";
    public final static String TIME_FORMAT = "HHmmss";
    public final static String MONTH_FORMAT = "yyyyMM";
    public final static String WEB_MONTH_FORMAT = "yyyy-MM";
    public final static String CHINESE_MONTH_FORMAT = "yyyy年MM月";
    public final static String CHINESE_DT_FORMAT = "yyyy年MM月dd日";
    public final static String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String NEW_FORMAT_LINE = "yyyy/MM/dd HH:mm:ss";
    public final static String NEW_FORMAT_SHORT = "yyyy-MM-dd";
    public final static String NEW_FORMAT_SHORT_2 = "yy-MM-dd";
    public final static String NEW_FORMAT_SHORT_3 = "MM-dd HH:mm";
    public final static String NEW_FORMAT_SHORT_4 = "MM-dd HH:mm:ss";
    public final static String MILLI_FORMAT = "yyyy-MM-dd HH:mm:ss:SS";
    public final static String NO_SECOND_FORMAT = "yyyy-MM-dd HH:mm";
    public final static long ONE_DAY_MILL_SECONDS = 86400000;
    public final static String MILLI_S_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    public final static String MILLI_S_FORMAT_1 = "yyyyMMddHHmmssSSS";
    public final static String MILLI_S_FORMAT_2 = "yyyy-MM-dd HH:mm:ss.SSS'Z'";
    public final static String STANDARD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public final static String DATEFORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
    public final static String DATEFORMAT2 = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);

        df.setLenient(false);
        return df;
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 获取指定年份和月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByYearsMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }


    public static Date parseDateNoTime(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(SHORT_FORMAT);

        if ((sDate == null) || (sDate.length() < SHORT_FORMAT.length())) {
            throw new ParseException("length too little", 0);
        }

        if (!StringUtils.isNumeric(sDate)) {
            throw new ParseException("not all digit", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static String getMilliFormat(Date date) {

        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(MILLI_FORMAT);
        try {
            return sdf.format(date);
        } catch (Exception e) {
        }
        return null;
    }

    public static Date parseDateNoTimeWebFormat(Date date) {

        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(WEB_FORMAT);
        String sDate = sdf.format(date);
        try {
            return sdf.parse(sDate);
        } catch (Exception e) {

        }
        return null;
    }

    public static Date parseDateWebMonthFormat(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(WEB_MONTH_FORMAT);
        String sDate = sdf.format(date);
        try {
            return sdf.parse(sDate);
        } catch (Exception e) {

        }
        return null;
    }

    public static Date parseDateWebMonthFormat(String date) {
        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(WEB_MONTH_FORMAT);
        try {
            return sdf.parse(date);
        } catch (Exception e) {

        }
        return null;
    }

    public static String parseDateNoTimeWebFormatStr(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(WEB_FORMAT);
        return sdf.format(date);

    }

    public static String parseDateStandardFormatStr(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(STANDARD_FORMAT);
        return sdf.format(date);

    }


    public static Date parseDateNoTime(Date date) {

        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(SHORT_FORMAT);
        String sDate = sdf.format(date);
        try {
            return sdf.parse(sDate);
        } catch (Exception e) {

        }
        return null;
    }

    public static Date parseDateNoTime(String sDate, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("Null format. ", 0);
        }

        DateFormat dateFormat = new SimpleDateFormat(format);

        if ((sDate == null) || (sDate.length() < format.length())) {
            throw new ParseException("length too little", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static String parseDateToString(Date sdate, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(sdate);
    }


    public static Date parseDateNoTimeAccurate(String sDate, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("Null format. ", 0);
        }

        DateFormat dateFormat = new SimpleDateFormat(format);

        if ((sDate == null) || (sDate.length() != format.length())) {
            throw new ParseException("长度错误", 0);
        }
        Date date = dateFormat.parse(sDate);
        String str = format(date, format);
        if (!sDate.equals(str)) {
            throw new ParseException("日期出错", 0);
        }

        return date;
    }

    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit)
            throws ParseException {
        sDate = sDate.replaceAll(delimit, "");

        DateFormat dateFormat = new SimpleDateFormat(SHORT_FORMAT);

        if ((sDate == null) || (sDate.length() != SHORT_FORMAT.length())) {
            throw new ParseException("length not match", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT);
        Date d = null;

        if ((sDate != null) && (sDate.length() == LONG_FORMAT.length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }

        return d;
    }

    public static Date parseDateStandardFormat(String sDate) {
        Date d = null;
        if ((sDate != null)) {
            try {
                if (sDate.length() > 19) {
                    sDate = sDate.substring(0, 19);
                }
                DateFormat dateFormat = new SimpleDateFormat(STANDARD_FORMAT);
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return d;
    }

    public static Date parseDateNewFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(NEW_FORMAT);
        DateFormat dateFormatLine = new SimpleDateFormat(NEW_FORMAT_LINE);
        Date d = null;
        if ((sDate != null)) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                try {
                    d = dateFormatLine.parse(sDate);
                } catch (Exception e) {
                    return null;
                }
                return d;
            }
        }
        return d;
    }

    public static Date parseDateMilliFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(MILLI_S_FORMAT);
        Date d = null;
        if ((sDate != null)) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return d;
    }

    public static Date parseDateFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT, Locale.UK);
        Date d = null;
        if ((sDate != null)) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return d;
    }

    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(betweenDays));
    }


    public static Date parseDateWevFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(WEB_FORMAT);
        DateFormat webDateFormatLine = new SimpleDateFormat(WEB_FORMAT_LINE);
        Date d = null;

        if ((sDate != null)) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                try {
                    d = webDateFormatLine.parse(sDate);
                } catch (Exception e) {
                    return null;
                }
                return d;
            }
        }

        return d;
    }


    public static Date goToBizDate(Date oldDate, Date bizDate) {
        long time = oldDate.getTime() % ONE_DAY_MILL_SECONDS;
        return new Date(bizDate.getTime() + time);
    }

    /**
     * 计算当前时间几小时之后的时间
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60);
    }

    /**
     * 计算当前时间几小时之后的时间
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours1(Date date, Double hours) {
        return addSeconds(date, new Double(hours * 3600).longValue());
    }

    /**
     * 计算当前时间几分钟之后的时间
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60);
    }

    /**
     * @param date1
     * @param secs
     * @return
     */

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + (secs * 1000));
    }


    /**
     * 取得新的日期
     *
     * @param date1 日期
     * @param days  天数
     * @return 新的日期
     */
    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }

    /**
     * 将日期增加n个月,
     * 若n月后没有改日期，则为该月最后一日，不改变月份计算。如1-31日加一个月为2-28
     *
     * @param dt
     * @param months
     * @return
     */
    public static Date addMonths(Date dt, int months) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(dt);
        //增加一个月
        cd.add(Calendar.MONTH, months);
        return cd.getTime();
    }

    /**
     * 将日期增加n年,
     * 若n年后没有改日期，则为该月最后一日，不改变月份。如2012-2-29日加一个年为2013-2-28
     *
     * @param dt
     * @param years
     * @return
     */
    public static Date addYears(Date dt, int years) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(dt);
        //增加一年
        cd.add(Calendar.YEAR, years);
        return cd.getTime();
    }


    public static String getTomorrowDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    public static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT);

        return getDateString(date, dateFormat);
    }

    public static String getNewFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(NEW_FORMAT);
        return getDateString(date, dateFormat);
    }

    public static String getStandarDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(STANDARD_FORMAT);

        return getDateString(date, dateFormat);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    public static String getYesterDayDateString(String sDate) throws ParseException {
        Date aDate = parseDateNoTime(sDate);

        aDate = addSeconds(aDate, -ONE_DAY_SECONDS);

        return getDateString(aDate);
    }

    /**
     * @return 当天的时间格式化为"yyyyMMdd"
     */
    public static String getDateString(Date date) {
        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        return df.format(date);
    }

    public static String getDateString2(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        return df.format(date);
    }

    public static String getWebDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(WEB_FORMAT);

        return getDateString(date, dateFormat);
    }

    /**
     * 取得“X年X月X日”的日期格式
     *
     * @param date
     * @return
     */
    public static String getChineseDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat(CHINESE_DT_FORMAT);

        return getDateString(date, dateFormat);
    }

    public static String getTodayString() {
        DateFormat dateFormat = getNewDateFormat(SHORT_FORMAT);

        return getDateString(new Date(), dateFormat);
    }

    public static String getTimeString(Date date) {
        DateFormat dateFormat = getNewDateFormat(TIME_FORMAT);

        return getDateString(date, dateFormat);
    }

    public static String getBeforeDayString(int days) {
        Date date = new Date(System.currentTimeMillis() - (ONE_DAY_MILL_SECONDS * days));
        DateFormat dateFormat = getNewDateFormat(SHORT_FORMAT);

        return getDateString(date, dateFormat);
    }

    /**
     * 取得两个日期间隔秒数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数
     */
    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            sysDate.setTime(one);
        } else {
            sysDate.setTime(two);
        }
        Calendar failDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            failDate.setTime(two);
        } else {
            failDate.setTime(one);
        }
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    /**
     * 取得两个日期间隔秒数（日期1-日期2）
     *
     * @param one 日期1
     * @param two 日期2
     * @return 间隔秒数（正数日期1大于日期2，负数日期1小于日期2）
     */
    public static long getDiffSeconds2(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    /**
     * 取得两个日期的间隔分钟数（日期1-日期2）
     *
     * @param one
     * @param two
     * @return 间隔分钟数
     */
    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            sysDate.setTime(one);
        } else {
            sysDate.setTime(two);
        }
        Calendar failDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            failDate.setTime(two);
        } else {
            failDate.setTime(one);
        }
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 1000);
    }

    /**
     * 描述:  取得两个日期的间隔分钟数（日期1-日期2) 可能为负
     *
     * @param one
     * @param two
     * @return 分钟数
     * @author WeiXun
     * date 2020/5/9
     * --------------------------------------------------
     * 修改人     修改日期    修改描述
     * WeiXun 2020/5/9 15:29 创建
     * --------------------------------------------------
     */
    public static long getDiffMinutesNew(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (60 * 1000);
    }

    /**
     * 取得两个日期的间隔小时数（日期1-日期2）
     *
     * @param one
     * @param two
     * @return 间隔小时数
     */
    public static Double getDiffHours(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            sysDate.setTime(one);
        } else {
            sysDate.setTime(two);
        }
        Calendar failDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            failDate.setTime(two);
        } else {
            failDate.setTime(one);
        }
        Double hours = Double.parseDouble(sysDate.getTimeInMillis() - failDate.getTimeInMillis() + "");
        hours = hours / 60 / 60 / 1000;
        return hours;
    }

    /**
     * 取得两个日期的间隔天数（日期1-日期2）
     *
     * @param one
     * @param two
     * @return 间隔天数
     */
    public static Integer getDiffDays(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            sysDate.setTime(one);
        } else {
            sysDate.setTime(two);
        }
        sysDate.set(11, 0);
        sysDate.set(12, 0);
        sysDate.set(13, 0);
        sysDate.set(14, 0);
        Calendar failDate = new GregorianCalendar();
        if (one.getTime() > two.getTime()) {
            failDate.setTime(two);
        } else {
            failDate.setTime(one);
        }
        failDate.set(11, 0);
        failDate.set(12, 0);
        failDate.set(13, 0);
        failDate.set(14, 0);
        return Integer.parseInt((sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (24 * 60 * 60 * 1000) + "");
    }

    /**
     * 获取指定日期的星期
     *
     * @return
     */
    public static int getWeek(Calendar calendar) {
        // 1--7的值,对应：星期日，星期一，星期二，星期三....星期六
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    public static Integer getDiffDays2(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        /*if(one.getTime() > two.getTime()){
        	sysDate.setTime(one);
        }else{
        	sysDate.setTime(two);
        }*/
        sysDate.setTime(one);
        sysDate.set(11, 0);
        sysDate.set(12, 0);
        sysDate.set(13, 0);
        sysDate.set(14, 0);
        Calendar failDate = new GregorianCalendar();
        /*if(one.getTime() > two.getTime()){
        	failDate.setTime(two);
        }else{
        	failDate.setTime(one);
        }*/
        failDate.setTime(two);
        failDate.set(11, 0);
        failDate.set(12, 0);
        failDate.set(13, 0);
        failDate.set(14, 0);
        return Integer.parseInt((sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / (24 * 60 * 60 * 1000) + "");
    }

    public static String getBeforeDayString(String dateString, int days) {
        Date date;
        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }

        date = new Date(date.getTime() - (ONE_DAY_MILL_SECONDS * days));

        return df.format(date);
    }

    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate.length() != SHORT_FORMAT.length()) {
            return false;
        }

        try {
            //---- 避免日期中输入非数字 ----
            Integer.parseInt(strDate);
        } catch (Exception numberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidShortDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidShortDateFormat(temp);
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     *
     * @param strDate
     * @return
     */
    public static boolean isValidLongDateFormat(String strDate) {
        if (strDate.length() != LONG_FORMAT.length()) {
            return false;
        }

        try {
            //---- 避免日期中输入非数字 ----
            Long.parseLong(strDate);
        } catch (Exception numberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat(LONG_FORMAT);

        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidFullDateFormat(String strDate) {
        DateFormat df = getNewDateFormat(NEW_FORMAT);
        try {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    /**
     * 判断表示时间的字符是否为符合yyyyMMddHHmmss格式
     *
     * @param strDate
     * @param delimiter
     * @return
     */
    public static boolean isValidLongDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidLongDateFormat(temp);
    }

    public static String getShortDateString(String strDate) {
        return getShortDateString(strDate, "-|/");
    }

    public static String getShortDateString(String strDate, String delimiter) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        String temp = strDate.replaceAll(delimiter, "");

        if (isValidShortDateFormat(temp)) {
            return temp;
        }

        return null;
    }

    public static String getShortFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(SHORT_FORMAT);

        return df.format(cal.getTime());
    }

    public static String getWebTodayString() {
        DateFormat df = getNewDateFormat(WEB_FORMAT);

        return df.format(new Date());
    }

    public static String getWebFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        DateFormat df = getNewDateFormat(WEB_FORMAT);

        return df.format(cal.getTime());
    }

    public static String getEmailDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String getSmsDate(Date today) {
        String todayStr;
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");

        todayStr = sdf.format(today);
        return todayStr;
    }

    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(MONTH_FORMAT).format(date);
    }

    /**
     * 获取系统日期的前一天日期，返回Date
     *
     * @return
     */
    public static Date getBeforeDate() {
        Date date = new Date();

        return new Date(date.getTime() - (ONE_DAY_MILL_SECONDS));
    }

    /**
     * 获得指定时间当天起点时间
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);

        String dateString = df.format(date);

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * 获得指定时间当天结束时间
     *
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }


    /**
     * 获得当天，不带时间，仅日期
     *
     * @return
     */
    public static Date getToday() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);

        String dateString = df.format(new Date());

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 获得当天，不带时间，仅日期
     *
     * @return
     */
    public static Date getToday2() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);

        String dateString = df.format(new Date());

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 判断参date上min分钟后，是否小于当前时间
     *
     * @param date
     * @param min
     * @return
     */
    public static boolean dateLessThanNowAddMin(Date date, long min) {
        return addMinutes(date, min).before(new Date());

    }

    public static boolean isBeforeNow(Date date) {
        if (date == null) {
            return false;
        }
        return date.compareTo(new Date()) < 0;
    }

    public static Date parseNoSecondFormat(String sDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(NO_SECOND_FORMAT);

        if ((sDate == null) || (sDate.length() < NO_SECOND_FORMAT.length())) {
            throw new ParseException("length too little", 0);
        }

        return dateFormat.parse(sDate);
    }

    public static int getDayNum(Date dateBegin, Date dateEnd) {
        // 判断顺序，避免结果为负数
        if (dateBegin.getTime() > dateEnd.getTime()) {
            Date tempDate = dateBegin;
            dateBegin = dateEnd;
            dateEnd = tempDate;
        }
        return (int) ((dateEnd.getTime() - dateBegin.getTime()) / (60 * 60 * 24 * 1000));
    }

    public static int getMinuteNum(Date dateBegin, Date dateEnd) {
        // 判断顺序，避免结果为负数
        if (dateBegin.getTime() > dateEnd.getTime()) {
            Date tempDate = dateBegin;
            dateBegin = dateEnd;
            dateEnd = tempDate;
        }
        return (int) ((dateEnd.getTime() - dateBegin.getTime()) / (60 * 1000));
    }

    public static int dateNum(Date dateBegin, Date dateEnd) {
        Date b = parseDateNoTime(dateBegin);
        Date e = parseDateNoTime(dateEnd);

        return Math.round((e.getTime() - b.getTime()) / (60 * 60 * 24 * 1000));
    }

    /**
     * 根据日期段，和指定的星期几，获得相应的日期
     *
     * @param weeks     1234560,星期天用0表示
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public static List<Date> getDates(String weeks, Date dateBegin, Date dateEnd) {

        List<Date> dateList = new ArrayList<Date>();

        if (weeks == null) {
            weeks = "";
        }
        if (dateBegin == null || dateEnd == null) {
            return dateList;
        }
        if (dateBegin.after(dateEnd)) {
            return dateList;
        }
        GregorianCalendar begin = new GregorianCalendar();
        begin.setTime(dateBegin);
        GregorianCalendar end = new GregorianCalendar();
        end.setTime(dateEnd);

        while (begin.getTimeInMillis() <= end.getTimeInMillis()) {

            String week = String.valueOf(begin.get(Calendar.DAY_OF_WEEK) - 1);
            if (weeks.indexOf(week) != -1) {
                dateList.add(begin.getTime());
            }

            begin.add(Calendar.DATE, 1);

        }
        return dateList;
    }

    /**
     * 字符串转换成日期，不含时间
     *
     * @param sDate
     * @param format 若有format，则不考虑默认格式；若format为null，则支持默认格式
     *               默认格式：yyyy.MM.dd   yyyy/MM/dd   yyyy-MM-dd  yyyyMMdd
     * @return
     */
    public static Date parseString(String sDate, String format) {

        if (StringUtils.isBlank(sDate)) {
            return null;
        }

        if (format != null) {
            try {
                return parseDateNoTime(sDate, format);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        String tmp = sDate;
        tmp = tmp.replace(".", "");
        tmp = tmp.replace("/", "");
        tmp = tmp.replace("-", "");
        try {
            return parseDateNoTime(tmp);
        } catch (ParseException e) {
            try {
                return parseString(sDate);
            } catch (ParseException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    /**
     * @param sDate 日期字符串 用".","/","-"分隔
     * @return
     * @throws ParseException
     */
    public static Date parseString(String sDate) throws ParseException {
        boolean right = false;
        String tmp1 = sDate;
        String delimit = "-";
        if (tmp1.indexOf('.') > -1) {
            delimit = "\\.";
            right = true;
        } else if (tmp1.indexOf('/') > -1) {
            delimit = "/";
            right = true;
        } else if (tmp1.indexOf('-') > -1) {
            right = true;
        }
        if (right) {
            String[] s = tmp1.split(delimit);
            if (s.length != 3) {
                throw new ParseException("length too little", 0);
            }
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(s[0]), Integer.parseInt(s[1]) - 1, Integer.parseInt(s[2]));
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        } else {
            throw new ParseException("data format is wrong ", 0);
        }
    }

    /**
     * 获取之间的日期，只能是营业日期用
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> dateBewteen(Date startDate, Date endDate) {
        int i = dateNum(startDate, endDate);
        if (i >= 0) {
            List<Date> l = new ArrayList<Date>();
            l.add(startDate);
            Date date = startDate;
            for (int k = 0; k < i; k++) {
                date = addDays(date, 1);
                l.add(date);
            }
            return l;
        }
        return null;
    }

    /**
     * 返回给定日期周几
     * 0：周日
     * 1：周一
     * 2：周二
     * 3：周三
     * 4：周四
     * 5：周五
     * 6：周六
     *
     * @param date 日期
     * @return int
     * @author Cairo
     * @date 2013-09-23
     */
    public static int getDateWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY) {
            return 6;
        } else if (day == Calendar.SUNDAY) {
            return 0;
        } else if (day == Calendar.MONDAY) {
            return 1;
        } else if (day == Calendar.TUESDAY) {
            return 2;
        } else if (day == Calendar.WEDNESDAY) {
            return 3;
        } else if (day == Calendar.THURSDAY) {
            return 4;
        } else {
            return 5;
        }
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getMonthBefore(Date date) {
        long time = date.getTime();
        long preTime = time - 30 * 24 * 60 * 60 * 1000L;
        Date preDay = new Date();
        preDay.setTime(preTime);
        return preDay;
    }

    public static Date getMonthAfter(Date date) {
        long time = date.getTime();
        long preTime = time + 30 * 24 * 60 * 60 * 1000L;
        Date preDay = new Date();
        preDay.setTime(preTime);
        return preDay;
    }

    public static Date getDateBefore(Date date) {
        long time = date.getTime();
        long preTime = time - 24 * 60 * 60 * 1000L;
        Date preDay = new Date();
        preDay.setTime(preTime);
        return preDay;
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return gc;
    }

    public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }

    public static Date getBirth(Date dt) {
        if (dt == null) {
            return null;
        }

        Calendar cd = new GregorianCalendar();
        cd.setTime(dt);
        cd.set(Calendar.HOUR, 12);
        cd.set(Calendar.MINUTE, 0);
        cd.set(Calendar.SECOND, 0);
        cd.set(Calendar.MILLISECOND, 0);

        return cd.getTime();
    }

    /**
     * 获取给定时间小时数
     * 0-23
     *
     * @param date
     * @return int
     * @author Cairo
     * @date 2014-10-31
     */
    public static int getHours(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取给定时间分钟数
     * 0-59
     *
     * @param date
     * @return int
     */
    public static int getMins(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取月初日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMinMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取月末日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMaxMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取年初日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMinYearDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * 获取年末日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMaxYearDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * 算出明天加房费的具体时间 在明天加房费具体时间入住的才要加房费
     *
     * @param bizDate
     * @param addStr
     * @return
     */
    public static Date getNextAddDate(Date bizDate, String addStr) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bizDate);
        cal.add(Calendar.DATE, 1);
        if (addStr != null && !"".equals(addStr) && addStr.length() >= 5) {
            cal.set(Calendar.HOUR, Integer.parseInt(addStr.substring(0, addStr.indexOf(":"))));
            cal.set(Calendar.MINUTE, Integer.parseInt(addStr.substring(addStr.indexOf(":") + 1, addStr.length())));
        }
        return cal.getTime();
    }

    public static Date parseDateTime(Date date) {

        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(NEW_FORMAT);
        String sDate = sdf.format(date);
        try {
            return sdf.parse(sDate);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 将日期增加n天,
     *
     * @param dt
     * @param day
     * @return
     */
    public static Date addDays(Date dt, int day) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(dt);
        // 增加一天
        cd.add(Calendar.DAY_OF_MONTH, day);
        return cd.getTime();
    }

    public static Date parseDate(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(NEW_FORMAT_SHORT);
        String sDate = sdf.format(date);
        try {
            return sdf.parse(sDate);
        } catch (Exception e) {

        }
        return null;
    }

    public static String getDateStringLong() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(date);
    }

    /**
     * 获取时间段内 按interval间隔的时间，包含endDate
     *
     * @param beginDate
     * @param endDate
     * @param interval
     * @return
     */
    public static List<Date> getIntervalDate(Date beginDate, Date endDate, int interval) {
        List<Date> dateList = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        while (calBegin.before(calEnd) || calBegin.equals(calEnd)) {
            dateList.add(calBegin.getTime());
            calBegin.add(Calendar.DAY_OF_MONTH, interval);
        }
        return dateList;
    }

    public static List<Date> getIntervalMonth(Date beginDate, Date endDate, int interval) {
        List<Date> dateList = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(beginDate);
        calBegin.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        calEnd.set(Calendar.DAY_OF_MONTH, 1);
        while (calBegin.before(calEnd) || calBegin.equals(calEnd)) {
            dateList.add(calBegin.getTime());
            calBegin.add(Calendar.MONTH, interval);
        }
        return dateList;
    }

    public static int getDiffMonth(Date date1, Date date2) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(date1);
        end.setTime(date2);
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    public static int getDiffMonthNumber(Date date1, Date date2) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(date1);
        end.setTime(date2);
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return month + result;
    }

    public static void main(String[] args) {
        Date datebegin = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(datebegin);
        instance.add(Calendar.DAY_OF_YEAR, 234);
        Date dateEnd = instance.getTime();
        List<Date> intervalMonth = getIntervalMonth(datebegin, dateEnd, 1);
        for (Date date : intervalMonth) {
            System.out.println(format(date, NEW_FORMAT));
        }
    }

}
