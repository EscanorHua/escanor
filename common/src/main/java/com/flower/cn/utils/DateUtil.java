package com.flower.cn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {


    public  static  final String DEFAULT_DATE_PATTERN="yyyy-MM-dd HH:mm:ss";

    public static String timeTransfer(Long timeSec) {
        Long mili = timeSec % 1000;
        timeSec = timeSec / 1000;
        Long sec = timeSec % 60;
        Long min = timeSec / 60;
        if (min == 0) return sec + "." + mili + "s";
        return min + "m" + sec + "." + mili + "s";
    }
    
    public static String getStandardStr() {
		return getDateStr("yyyy-MM-dd HH:mm:ss");
	}

    public static String getDateNum() {
        return getDateStr("yyyyMMdd");
    }

    public static String getDateNum(Date date) {
        return getDateStr("yyyyMMdd", date);
    }

    public static Date getDateByDay(Integer day) {
        Calendar ca = new GregorianCalendar();
        ca.add(Calendar.DAY_OF_MONTH, day);
        return ca.getTime();
    }

    public static String getTimeNum() {
        return getDateStr("HHmmss");
    }
	
	public static String getDateStr(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

    public static String getDateStr(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static final void main(String... args) {
        System.out.println(getDateNum(getDateByDay(-1)));
    }

    public static String getDefaultDatePattern(){
        return  DEFAULT_DATE_PATTERN;
    }
    public static String dateDefaultFormat(Date date){
        return  getDateStr(getDefaultDatePattern(),date);
    }
    public static Date stringDefaultToDate(Object dateString){
        SimpleDateFormat format = new SimpleDateFormat(getDefaultDatePattern());
        Date date =null;
        try {
            date=  format.parse(dateString.toString());
        } catch (Exception e) {
        }
        return  date;
    }
    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1
     *            时间1 格式yyyy-MM-dd
     * @param date2
     *            时间2 格式yyyy-MM-dd
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String date1, String date2) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date1));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(date2));
        long time2 = cal.getTimeInMillis();
        long between_days = Math.abs((time2 - time1) / (1000 * 3600 * 24));

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前月最后一天
     * */
    public static int monthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return Integer.parseInt(last);
    }
}
