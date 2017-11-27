package molab.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Molab {
	
	public static String parseToday() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
	}
	
	public static String parseYesterday() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date(System.currentTimeMillis() - 86400000l)); // 24*60*60*1000
	}
	
	public static Long parseYesterdayStart() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis() - 86400000l);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static Long parseYesterdayEnd() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis() - 86400000l);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTimeInMillis();
	}
	
	public static String parseMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		return format.format(new Date());
	}
	
	public static double rescale(double d) {
		return rescale(d, 2);
	}
	
	private static double rescale(double d, int scale) {
		String pattern = "0.";
		for (int i = 0; i < scale; i++) {
			pattern += "#";
		}
		DecimalFormat format = new DecimalFormat(pattern);
		return Double.valueOf(format.format(d));
	}

}
