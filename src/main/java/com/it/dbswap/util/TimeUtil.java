//package com.it.dbswap.util;
//
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.FastDateFormat;
//
//public class TimeUtil {
//    public  static final String DATE_HOUR_PATTERN = "HH";
//    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
//    public static final String DATE_TIME_WITH_ZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
//    public static final String DATE_PATTERN = "yyyy-MM-dd";
//    private static final FastDateFormat DATE_HOUR_DF;
//    private static final FastDateFormat DATE_TIME_DF;
//    private static final FastDateFormat DATE_DF;
//    private static final FastDateFormat DATE_TIME_WITH_ZONE_DF;
//
//    public TimeUtil() {
//    }
//
//    public static String getDateAsStr(int day) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(5, day);
//        return DATE_DF.format(cal.getTime());
//    }
//
//    public static boolean isAfter(Date date, String dateTimeStr) {
//        Date date2;
//        try {
//            date2 = DATE_TIME_DF.parse(dateTimeStr);
//        } catch (ParseException var4) {
//            return true;
//        }
//
//        return date.before(date2);
//    }
//
//    public static String getLastTimeOfToday() {
//        return toDateStr(new Date()) + " 23:59:59";
//    }
//
//    public static boolean isAfter(String time1, String time2) {
//        return StringUtils.compare(time1, time2) > 0;
//    }
//
//    public static boolean isAfter1800(String dateTime) {
//        return dateTime.substring(11, 19).compareTo("18:00:00") > 0;
//    }
//
//    public static String toDateStr(Date date) {
//        return DATE_DF.format(date);
//    }
//
//    public static String toDateStrWithZone(Date date) {
//        return DATE_TIME_WITH_ZONE_DF.format(date);
//    }
//
//    public static String toTimeStr(Date date) {
//        return DATE_TIME_DF.format(date);
//    }
//
//    public static int getDayOfWeek(String timeStr) throws ParseException {
//        Date date = DATE_TIME_DF.parse(timeStr);
//        return getDayOfWeek(date);
//    }
//
//    public static int getDayOfWeek(Date date) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        return cal.get(7) - 1;
//    }
//
//    public static int getCurDayOfWeek() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        return cal.get(7) - 1;
//    }
//
//    public static long diffSec(String time1, String time2) throws ParseException {
//        Date date1 = DATE_TIME_DF.parse(time1);
//        Date date2 = DATE_TIME_DF.parse(time2);
//        return (date1.getTime() - date2.getTime()) / 1000L;
//    }
//
//    public static String getDateStr(String timeStr) {
//        return !StringUtils.isBlank(timeStr) && timeStr.length() >= 10 ? timeStr.substring(0, 10) : null;
//    }
//
//    public static boolean isSameDay(String timeStr1, String timeStr2) {
//        return StringUtils.equals(getDateStr(timeStr1), getDateStr(timeStr2));
//    }
//
//    public static boolean isLessEight(String time) throws ParseException {
//        Date date = DATE_TIME_DF.parse(time);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int hour = cal.get(11);
//        return hour >= 0 && hour <= 7;
//    }
//
//    public static String addTm(String time, int field, int value) {
//        Date date = null;
//
//        try {
//            date = DATE_TIME_DF.parse(time);
//        } catch (ParseException var5) {
//            return "";
//        }
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(field, value);
//        return DATE_TIME_DF.format(cal.getTime());
//    }
//
//    public static String longToString(Long time) {
//        return time != null ? DATE_TIME_DF.format(new Date(time)) : null;
//    }
//
//    public static List<String> getStatsTm() {
//        Date date = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int hour = cal.get(11);
//        cal.add(5, -4);
//        String fiveDay = DATE_DF.format(cal.getTime()).concat(" 00:00:00");
//        String today = DATE_DF.format(date);
//        String startTm = today.concat(" 00:00:00");
//        String endTm = today.concat(" " + hour + ":00:00");
//        List<String> timeList = new ArrayList();
//        timeList.add(startTm);
//        timeList.add(endTm);
//        timeList.add(fiveDay);
//        return timeList;
//    }
//
//    public static List<String> getStatsTm(String day, String hour) {
//        Date dayDate = null;
//
//        try {
//            dayDate = DATE_DF.parse(day);
//        } catch (ParseException var9) {
//
//        }
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dayDate);
//        cal.add(5, -4);
//        String fiveDay = DATE_DF.format(cal.getTime()).concat(" 00:00:00");
//        String today = DATE_DF.format(dayDate);
//        String startTm = today.concat(" 00:00:00");
//        String endTm = today.concat(" " + hour + ":00:00");
//        List<String> timeList = new ArrayList();
//        timeList.add(startTm);
//        timeList.add(endTm);
//        timeList.add(fiveDay);
//        return timeList;
//    }
//
//    public static String getFiveDayStr(String time) {
//        Date fiveDay = null;
//
//        try {
//            fiveDay = DATE_DF.parse(time);
//        } catch (ParseException var3) {
//            return "";
//        }
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(fiveDay);
//        cal.add(5, -4);
//        return DATE_DF.format(cal.getTime());
//    }
//
//    public static Date addDay(Date date, int amount) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(5, amount);
//        return calendar.getTime();
//    }
//
//    public static boolean compareDate(Date date1, Date date2) {
//        return date1.getTime() > date2.getTime();
//    }
//
//    public static String date2String(Date date, String pattern) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        return simpleDateFormat.format(date);
//    }
//
//    public static Date string2Date(String dateString, String pattern) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//
//        try {
//            Date date = simpleDateFormat.parse(dateString);
//            return date;
//        } catch (Exception var5) {
//            return null;
//        }
//    }
//
//    public static List<String> getStartEndNowTm(String day) {
//        Date date = new Date();
//        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String stime = " 00:00:00";
//        String etime = " 23:59:59";
//        String startTime = day.concat(stime);
//        String endTime = day.concat(etime);
//        String timeNow = dateformat1.format(date);
//        int index = timeNow.indexOf(" ");
//        String time = timeNow.substring(index);
//        timeNow = day + time;
//        List<String> result = new ArrayList();
//        result.add(startTime);
//        result.add(endTime);
//        result.add(timeNow);
//        return result;
//    }
//
//    public static String getHourFromDate(String dateStr) {
//        try {
//            Date date = DATE_TIME_DF.parse(dateStr);
//            return DATE_HOUR_DF.format(date);
//        } catch (ParseException var2) {
//            return "";
//        }
//    }
//
//    public static void main(String[] args) {
//        System.out.println(addDay(new Date(), -1));
//    }
//
//    static {
//        DATE_HOUR_DF = FastDateFormat.getInstance(DATE_HOUR_PATTERN);
//        DATE_TIME_DF = FastDateFormat.getInstance(DATE_TIME_PATTERN);
//        DATE_DF = FastDateFormat.getInstance(DATE_PATTERN);
//        DATE_TIME_WITH_ZONE_DF = FastDateFormat.getInstance(DATE_TIME_WITH_ZONE_PATTERN);
//    }
//}
