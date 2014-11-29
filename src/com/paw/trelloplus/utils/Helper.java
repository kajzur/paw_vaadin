package com.paw.trelloplus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Helper {
	public static String getCurrentDateAsString() {
		Date dt = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String currentTime = sdf.format(dt);
		return currentTime;
	}
	
	public static String taskIdLpMapToSqlString(Map<String, Integer> data){
		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<String,Integer>> set = data.entrySet();
		Iterator<Map.Entry<String,Integer>> iteratorForSet = set.iterator();
		
		while(iteratorForSet.hasNext())
		{
			Map.Entry<String,Integer> entry = iteratorForSet.next();
			String tid = entry.getKey();
			int lp = entry.getValue();
			sb.append(" WHEN "+tid+" THEN "+lp);
		}
		
		return sb.toString();
	}
}
