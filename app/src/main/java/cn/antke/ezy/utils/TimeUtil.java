package cn.antke.ezy.utils;

import com.common.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具
 *
 * @author tianzhenhai 2015-3-5
 */
public class TimeUtil {

	// 时间格式模板
	/**
	 * yyyy-MM-dd
	 */
	public static final String TIME_FORMAT_ONE = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String TIME_FORMAT_TWO = "yyyy-MM-dd HH:mm";
	/**
	 * yyyy-MM-dd HH:mmZ
	 */
	public static final String TIME_FORMAT_THREE = "yyyy-MM-dd HH:mmZ";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String TIME_FORMAT_FOUR = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss.SSSZ
	 */
	public static final String TIME_FORMAT_FIVE = "yyyy-MM-dd HH:mm:ss.SSSZ";
	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 */
	public static final String TIME_FORMAT_SIX = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	/**
	 * HH:mm:ss
	 */
	public static final String TIME_FORMAT_SEVEN = "HH:mm:ss";
	/**
	 * HH:mm:ss.SS
	 */
	public static final String TIME_FORMAT_EIGHT = "HH:mm:ss.SS";
	/**
	 * yyyy.MM.dd
	 */
	public static final String TIME_FORMAT_9 = "yyyy.MM.dd";
	/**
	 * MM月dd日
	 */
	public static final String TIME_FORMAT_10 = "MM月dd日";
	public static final String TIME_FORMAT_11 = "MM-dd HH:mm";
	public static final String TIME_FORMAT_12 = "yyMM";
	/**
	 * HH:mm
	 */
	public static final String FORMAT13 = "HH:mm";
	public static final String TIME_FORMAT_14 = "M月d日";
	public static final String TIME_FORMAT_15 = "yyyy.MM.dd HH:mm:ss";
	public static final String TIME_FORMAT_16 = "yyyy/MM/dd HH:mm:ss";
	public static final String TIME_FORMAT_17 = "yyyy-MM-dd\nHH:mm";
	public static final String TIME_FORMAT_18 = "yyyy.MM.dd HH:mm";

	public final static String SIMPLE_DATE_PATTERN = "yyyyMMdd";

	/**
	 * 根据时间格式获得当前时间
	 */
	public static String getCurrentTime(String formater) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(formater, Locale.SIMPLIFIED_CHINESE);
		return dateFormat.format(date);
	}

	/**
	 * 格式化时间
	 */
	public static String formatTime(long time, String format) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time);
//        TimeZone timeZone = calendar.getTimeZone();
		return new SimpleDateFormat(format).format(new Date(time));
	}

	/**
	 * 判断是否是合法的时间
	 */
	public static boolean isValidDate(String dateString, String format) {
		return parseTime(dateString, format) > -1;
	}

	/**
	 * 日期转换
	 */
	public static long parseTime(String dateString, String format) {
		if (dateString == null || dateString.length() == 0) {
			return -1;
		}
		try {
			return new SimpleDateFormat(format).parse(dateString).getTime();
		} catch (ParseException e) {

		}
		return -1;
	}

	public static int getDaysBetween(String date1, String date2, String format) {
		return getDaysBetween(parseTime(date1, format), parseTime(date2, format));
	}

	public static int getDaysBetween(long date1, long date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(date1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(date2);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);

		return (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 3600 * 1000));
	}

	/**
	 * 获取时间戳 ，格式2010-1-4 16:21:4，如果是一位数的话，那么前面不加0
	 */
	public static String getTimeStamp() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		return (year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
	}

	/**
	 * Unix时间戳转换成日期
	 */
	public static String TimeStamp2Date(String timestampString, String formater) {
		Long timestamp = StringUtil.parseLong(timestampString, 0) * 1000;
		String date = new SimpleDateFormat(formater, Locale.SIMPLIFIED_CHINESE).format(new Date(timestamp));
		return date;
	}

	public static long getTodayTimeMillis() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static String getTimeByLong4Msg(long tLong) {
		String strDate = "";
		tLong = tLong * 1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tLong);
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
		// strDate = cal.getTime().toLocaleString();
		strDate = sdf.format(cal.getTime());
		return strDate;
	}

	public static String getTimeByLong(long tLong) {
		String strDate = "";
		tLong = tLong * 1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tLong);
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		// strDate = cal.getTime().toLocaleString();
		strDate = sdf.format(cal.getTime());
		return strDate;
	}

	public static String getTimeByLong(long tLong, String format) {
		String strDate = "";
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tLong);
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		strDate = sdf.format(cal.getTime());
		return strDate;
	}

	public static String getFriendlyTime(long time) {
		long now = System.currentTimeMillis();
		long elapse = now - time;
		if (elapse < 60 * 1000) {
			return "1分钟前";
		} else if (elapse < 3600 * 1000) {
			return (elapse / 1000 / 60) + "分钟前";
		} else if (elapse < 24 * 3600 * 1000) {
			return (elapse / 1000 / 3600) + "小时前";
		} else if (elapse < 2 * 24 * 3600 * 1000) {
			return "昨天 " + formatTime(time, FORMAT13);
		} else if (elapse < 3 * 24 * 3600 * 1000) {
			return "前天 " + formatTime(time, FORMAT13);
		} else {
			return formatTime(time, TIME_FORMAT_10);
		}
	}

	public static String getFriendlyDate(long time) {
		String today = formatTime(System.currentTimeMillis(), TIME_FORMAT_ONE);
		long today0Clock = parseTime(today, TIME_FORMAT_ONE);

		long dayMill = 24 * 3600 * 1000;
		long delta = time - today0Clock;
		if (delta > 0 && delta < dayMill) {
			return "今天";
		} else if (delta > 0 && delta < 2 * dayMill) {
			return "明天";
		} else if (delta > 0 && delta < 3 * dayMill) {
			return "后天";
		} else if (delta < 0) {
			return -delta / dayMill + 1 + "天前";
		} else {
			return delta / dayMill + "天后";
		}
	}

	public static String getFriendlyTime2(long time) {
		long now = System.currentTimeMillis();
		long elapse = now - time;
		if (elapse < 60 * 1000) {
			return "1分钟前";
		} else if (elapse < 3600 * 1000) {
			return (elapse / 1000 / 60) + "分钟前";
		} else if (elapse < 24 * 3600 * 1000) {
			return (elapse / 1000 / 3600) + "小时前";
		} else if (elapse < 2 * 24 * 3600 * 1000) {
			return "昨天 " + formatTime(time, FORMAT13);
		} else if (elapse < 3 * 24 * 3600 * 1000) {
			return "前天 " + formatTime(time, FORMAT13);
		} else {
			return formatTime(time, TIME_FORMAT_11);
		}
	}

	/**
	 * 天气刷新时间
	 *
	 * @param time
	 * @return
	 */
	public static String getWeatherTime(long time) {
		long now = System.currentTimeMillis();
		long elapse = now - time;
		if (elapse < 60 * 1000) {
			return "刚刚";
		} else if (elapse < 3600 * 1000) {
			return (elapse / 1000 / 60) + "分钟前";
		} else if (elapse < 24 * 3600 * 1000) {
			return (elapse / 1000 / 3600) + "小时前";
		} else {
			return formatTime(time, TIME_FORMAT_11);
		}
	}

	public static Date getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	public static Date getNextDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	public static Date getNexttDate(Date date, int some) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, some);
		return calendar.getTime();
	}

	/**
	 * 格式化Date成字符串
	 */
	public static String getStringFromDate(Date date) {
		return date == null ? " " : new SimpleDateFormat("yyyyMMdd").format(date);
	}

	/**
	 * 将字符串转为Date
	 */
	public static Date getDateFromString(String strDate) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyyMMdd").parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getPreDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * @param time 时间，单位：秒
	 * @return 时分秒
	 */
	public static String getFormatTime2(long time) {
		StringBuilder sb = new StringBuilder();
		if (time < 60) {
			if (time < 10) {
				sb.append("00:0").append(time);
			} else {
				sb.append("00:").append(time);
			}
		} else if (time > 59 && time < 3600) {
			long sec = time % 60;
			long tMin = time / 60;
			if (tMin < 10) {
				sb.append("0").append(tMin);
			} else {
				sb.append(tMin);
			}
			if (0 == sec) {
				sb.append(":00");
			} else {
				if (sec < 10) {
					sb.append(":0").append(sec);
				} else {
					sb.append(":").append(sec);
				}
			}
		} else {
			long hour = time / 3600;
			long min = (time % 3600) / 60;
			long sec = (time % 3600) % 60;

			if (hour<10) {
				sb.append("0").append(hour);
			} else {
				sb.append(hour);
			}

			if (0 == min) {
				sb.append(":00");
			} else {
				if (min < 10) {
					sb.append(":0").append(min);
				} else {
					sb.append(":").append(min);
				}
			}

			if (0 == sec) {
				sb.append(":00");
			} else {
				if (sec < 10) {
					sb.append(":0").append(sec);
				} else {
					sb.append(":").append(sec);
				}
			}
		}
		return sb.toString();
	}
}
