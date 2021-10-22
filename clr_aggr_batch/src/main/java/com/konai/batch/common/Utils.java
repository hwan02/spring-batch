package com.konai.batch.common;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Utils {

	private static AtomicLong lastTime = new AtomicLong(Long.MIN_VALUE);

	public static String getUID(){
		long currentTime = System.currentTimeMillis();

		while (true){
			long current = lastTime.get();

			if(currentTime > current){
				if(lastTime.compareAndSet(current, currentTime)){
					break;
				}
			} else {
				if(lastTime.compareAndSet(current, current + 1)){
					currentTime = current + 1;
					break;
				}
			}
		}

		String id = Long.toString(currentTime, 36).toUpperCase();

		return id;
	}

	public static String addMonth(String date, int value){
		String addMonth = date;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		Date dt = null;

		try {
			dt = df.parse(addMonth);
			calendar.setTime(dt);
			calendar.add(Calendar.MONTH, value);
			addMonth = df.format(calendar.getTime());
		} catch(Exception e){
			log.error("Date calculate false. base_date[{}] add_value[{}]. Using date to input date[{}]", date, value, date);
		}

		return addMonth;
	}

	public static String addMonthFirstDay(String date, int value){
		String addMonth = date;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		Date dt = null;

		try {
			dt = df.parse(addMonth);
			calendar.setTime(dt);
			calendar.add(Calendar.MONTH, value);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			addMonth = df.format(calendar.getTime());
		} catch(Exception e){
			log.error("Date calculate false. base_date[{}] add_value[{}]. Using date to input date[{}]", date, value, date);
		}

		return addMonth;
	}
}
