package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
	public static String getCurrentDateAsString() {
		Date dt = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String currentTime = sdf.format(dt);
		return currentTime;
	}
}
