package org.nature.platform.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 日期工具类
 * 
 * @author hutianlong
 *
 */
public class DateTool {

	private DateTool() {}
	
	/**
	 * 开始时间 从指定时间的00:00:00
	 * @param startDate
	 * @return
	 */
	public static Date startDate(Date startDate){
		
		return new DateTime(startDate)
				.withHourOfDay(0)
				.withMinuteOfHour(0)
				.withSecondOfMinute(0)
				.withMillisOfSecond(0)
				.toDate();
	}
	
	public static Date startDate(String startDate){
		return new DateTime(convertDate(startDate))
				.withHourOfDay(0)
				.withMinuteOfHour(0)
				.withSecondOfMinute(0)
				.withMillisOfSecond(0)
				.toDate();
	}
	
	/**
	 * 结束时间 从指定时间的23:59:59
	 * @param endDate
	 * @return
	 */
	public static Date endDate(Date endDate){
		return new DateTime(endDate)
				.withHourOfDay(23)
				.withMinuteOfHour(59)
				.withSecondOfMinute(59)
				.withMillisOfSecond(59)
				.toDate();
	}
	
	public static Date endDate(String endDate){
		return new DateTime(convertDate(endDate))
				.withHourOfDay(23)
				.withMinuteOfHour(59)
				.withSecondOfMinute(59)
				.withMillisOfSecond(59)
				.toDate();
	}
	
	
	/**
	 * 判断给定时间是否小于当前时间
	 * @param date 指定时间
	 * @return 给定时间小于前当时间时返回true,否则返回flase
	 */
	public static boolean isisAfter(Date date){
		return DateTime.now().isAfter(new DateTime(date));
	}
	
	/**
	 * 判断给定时间是否大于当前时间，
	 * @param date 指定时间
	 * @return 给定时间大于当前时间时返回true,否则返回false
	 */
	public static boolean isBefore(Date date){
		return DateTime.now().isBefore(new DateTime(date));
	}
	
	/**
	 * 根据字符时间
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int daysBetween(String startDate,String endDate){
		Date d1 = convertDate(startDate);
		Date d2 = convertDate(endDate);
		return daysBetween(d1,d2);
	}
	
	
	/**
	 * 两个时间的相差天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int daysBetween(Date startDate,Date endDate){
		return Days.daysBetween(createDateTime(startDate), createDateTime(endDate)).getDays();
	}
	

	/**
	 * 返回指定年数, year为正数向后推迟1年，负数时向前提前1年
	 * @param date 指定时间
	 * @param year 
	 * @return
	 */
	public static Date year(Date date, int year) {
		return createDateTime(date).plusYears(year).toDate();
	}

	/**
	 * 返回指定年数, year为正数向后推迟1年，负数时向前提前1年,并且可格式化格式
	 * @param date 指定时间
	 * @param year 
	 * @return
	 */
	public static String year(Date date, int year, String format) {
		DateTime dateTime = createDateTime(date).plusYears(year);
		if (StringUtils.isEmpty(format)) {
			return dateTime.toString();
		} else {
			return dateTime.toString(format);
		}
	}
	
	/**
	 * 返回当前年数，year为正数向后推迟1年，负数时向前提前1年
	 * @param year
	 * @return
	 */
	public static Date nowYear(int year){
		return year(now(),year);
	}
	
	/**
	 * 返回当前年数，year为正数向后推迟1年，负数时向前提前1年,并且指定格式化
	 * @param year
	 * @param format
	 * @return
	 */
	public static String nowYear(int year,String format){
		return year(now(),year,format);
	}

	/**
	 * 获得当前时间的前后月数
	 * 
	 * @param month
	 *            指定天数 month为正数向后推迟1月，负数时向前提前1月
	 * @return
	 */
	public static Date nowMonth(int month) {
		return month(now(), month);
	}

	/**
	 * 返回指定时间的前后月数
	 * 
	 * @param date
	 *            指定时间
	 * @param month
	 *            前后月数
	 * @return
	 */
	public static Date month(Date date, int month) {
		return createDateTime(date).plusMonths(month).toDate();
	}

	/**
	 * 获得当前时间的前后日期,并处理完字符串
	 * 
	 * @param month
	 *            指定天数 day为正数向后推迟1天，负数时向前提前1天
	 * @param format
	 *            时间格式化，如果不需要格式化设为null
	 * @return
	 */
	public static String nowMonth(int month, String format) {
		return month(now(), month, format);
	}

	/**
	 * 指定时间的前后日期,并处理完字符串
	 * 
	 * @param date
	 * @param month
	 * @param format
	 * @return
	 */
	public static String month(Date date, int month, String format) {
		DateTime dateTime = createDateTime(date).plusMonths(month);
		if (StringUtils.isEmpty(format)) {
			return dateTime.toString();
		} else {
			return dateTime.toString(format);
		}
	}

	/**
	 * 返回指定日期的天数
	 * 
	 * @param date
	 *            指定日期
	 * @param day
	 *            前后天数
	 * @return 返回Date
	 */
	public static Date day(Date date, int day) {
		return createDateTime(date).plusDays(day).toDate();
	}

	/**
	 * 返回指定日期的天数
	 * 
	 * @param date
	 *            指定日期
	 * @param day
	 *            前后天数
	 * @param format
	 *            格式化
	 * @return 返回字符串形式
	 */
	public static String day(Date date, int day, String format) {
		DateTime dateTime = createDateTime(date).plusDays(day);
		if (StringUtils.isEmpty(format)) {
			return dateTime.toString();
		} else {
			return dateTime.toString(format);
		}
	}

	/**
	 * 获得当前时间的前后日期
	 * 
	 * @param day
	 *            指定天数 day为正数向后推迟1天，负数时向前提前1天
	 * @return
	 */
	public static Date nowDay(int day) {
		return day(now(), day);
	}

	/**
	 * 获得当前时间的前后日期,并处理完字符串
	 * 
	 * @param day
	 *            指定天数 day为正数向后推迟1天，负数时向前提前1天
	 * @param format
	 *            时间格式化，如果不需要格式化设为null
	 * @return
	 */
	public static String nowDay(int day, String format) {
		return day(now(), day, format);
	}

	/**
	 * 返回中国格式的时间,例如:2016年10月11日 20:09:12
	 * 
	 * @param week
	 *            true 显示星期几 否则不显示星期几
	 */
	public static String nowChineseTime(boolean week) {
		return nowChineseTime(now(), week);
	}

	public static String nowChineseTime(Date date, boolean week) {
		StringBuilder sb = new StringBuilder("yyyy年MM月dd日 HH:mm:ss");
		if (week)
			sb.append(" EE");
		return new DateTime(date).toString(sb.toString(), Locale.CHINESE);
	}

	/**
	 * 判断是否为闰月
	 * 
	 * @return
	 */
	public static boolean isLeap() {
		return new DateTime().monthOfYear().isLeap();
	}

	/**
	 * 返回当前日期
	 * 
	 * @return
	 */
	public static Date now() {
		return createDateTime().toDate();
	}

	/**
	 * 返回当前日期的字符串类型
	 * 
	 * @return
	 */
	public static String nowStr() {
		return createDateTime().toString();
	}

	/**
	 * 指定date转换成String
	 * 
	 * @param date
	 * @return
	 */
	public static String nowStr(Date date) {
		return createDateTime(date).toString();
	}

	/**
	 * 将当前日期转化成指定格式化日期后转成string类型
	 * 
	 * @param format
	 *            yyyy-MM-dd HH:mm:ss or yyyy-MM-dd and yyyy/MM/dd HH:mm:ss EE
	 * @return
	 */
	public static String nowStr(String format) {
		return createDateTime().toString(format);
	}

	/**
	 * 根据指定日期和格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String nowStr(Date date, String format) {
		return createDateTime(date).toString(format);
	}
	
	/**
	 * 根据指定日期和格式
	 * 
	 * @param calendar
	 * @param format
	 * @return
	 */
	public static String nowStr(Calendar calendar, String format) {
		return createDateTime(calendar.getTime()).toString(format);
	}

	/**
	 * 将字符串指定的时间转换成Date ,默认格式:yyyy-MM-dd
	 * 
	 * @param time
	 * @return
	 */
	public static Date convertDate(String time) {
		DateTimeFormatter formatter = formatter("yyyy-MM-dd");
		return formatter.parseDateTime(time).toDate();
	}
	
	/**
	 * 将字符串指定的时间转换成Date ,默认格式:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static Date convertDate(String time,String formater) {
		DateTimeFormatter formatter = formatter(formater);
		return formatter.parseDateTime(time).toDate();
	}

	/**
	 * 按指定格式返回一个DateTimeFormatter
	 * 
	 * @param format
	 *            yyyy-MM-dd HH:mm:ss EE or yyyy-MM-dd and yyyy/MM/dd HH:mm:ss
	 *            EE
	 * @return
	 */

	private static DateTimeFormatter formatter(String format) {
		return DateTimeFormat.forPattern(format);
	}

	private static DateTime createDateTime() {
		return DateTime.now();
	}

	private static DateTime createDateTime(Date date) {
		return new DateTime(date);
	}

}
