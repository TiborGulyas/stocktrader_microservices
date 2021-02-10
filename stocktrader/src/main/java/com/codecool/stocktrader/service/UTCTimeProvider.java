package com.codecool.stocktrader.service;

import com.codecool.stocktrader.component.DataInitializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UTCTimeProvider {

    private Map<String, Long> UTCTimeStamps = new HashMap<>();
    public Map<String, Long> provideUTCTimeStamps(String resolution){
        if (resolution.equals("1") || resolution.equals("5")){
            return provideUTCTimeStampsPerMin();
        } else if (resolution.equals("D")){
            return provideUTCTimeStampsPerDay();
        }
        return null;
    }

    private Map<String, Long> provideUTCTimeStampsPerDay() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        System.out.println("today is:"+dayOfWeek);

        Calendar calOpen = Calendar.getInstance();
        if (calOpen.get(Calendar.DAY_OF_MONTH) <=2 && calOpen.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            calOpen.set(Calendar.MONTH, calOpen.get(Calendar.MONTH)-1);
        } else if (calOpen.get(Calendar.DAY_OF_MONTH) <=2 && calOpen.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            calOpen.set(Calendar.MONTH, calOpen.get(Calendar.MONTH)-1);
        }
        calOpen.set(Calendar.HOUR_OF_DAY,15);
        calOpen.set(Calendar.MINUTE,30);
        calOpen.set(Calendar.SECOND,0);
        calOpen.set(Calendar.MILLISECOND,0);
        if (calOpen.get(Calendar.DAY_OF_YEAR) > 60) {
            calOpen.set(Calendar.DAY_OF_YEAR, calOpen.get(Calendar.DAY_OF_YEAR) - 60);
        } else {
            int countDaysBack = 365 - calOpen.get(Calendar.DAY_OF_YEAR);
            calOpen.set(Calendar.YEAR, calOpen.get(Calendar.YEAR)-1);
            calOpen.set(Calendar.DAY_OF_YEAR, calOpen.get(Calendar.DAY_OF_YEAR) - countDaysBack);
        }
        Calendar calClose = Calendar.getInstance();
        calClose.set(Calendar.HOUR_OF_DAY,22);
        calClose.set(Calendar.MINUTE,00);
        calClose.set(Calendar.SECOND,0);
        calClose.set(Calendar.MILLISECOND,0);


        UTCTimeStamps.put("from", calOpen.getTime().getTime()/1000);
        UTCTimeStamps.put("to", calClose.getTime().getTime()/1000);
        //SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        //Date dateNow = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(dateMorning));
        return UTCTimeStamps;
    }

    private Map<String, Long> provideUTCTimeStampsPerMin(){
        Calendar today = Calendar.getInstance();
        for (List<Map<String, Long>> holiday:DataInitializer.tradeHolidays.values()) {
            System.out.println("today: "+today.getTimeInMillis()+"start: "+holiday.get(0).get("start")+"end: "+holiday.get(0).get("end"));
            if (today.getTimeInMillis() > holiday.get(0).get("start") && today.getTimeInMillis() < holiday.get(0).get("end")){
                return holiday.get(1);
            }
        }

        UTCTimeStamps = new HashMap<>();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        System.out.println("today is:"+dayOfWeek);

        Calendar calOpen = Calendar.getInstance();
        calOpen.set(Calendar.HOUR_OF_DAY,15);
        calOpen.set(Calendar.MINUTE,30);
        calOpen.set(Calendar.SECOND,0);
        calOpen.set(Calendar.MILLISECOND,0);

        Calendar calClose = Calendar.getInstance();
        calClose.set(Calendar.HOUR_OF_DAY,22);
        calClose.set(Calendar.MINUTE,00);
        calClose.set(Calendar.SECOND,0);
        calClose.set(Calendar.MILLISECOND,0);

        if (dayOfWeek == 7) {
            System.out.println("dayofweek7 triggered");
            calOpen.set(Calendar.DAY_OF_WEEK, 6);
            calClose.set(Calendar.DAY_OF_WEEK, 6);
        } else if (dayOfWeek == 1) {
            System.out.println("dayofweek1 triggered");
            calOpen.set(Calendar.WEEK_OF_YEAR, calOpen.get(Calendar.WEEK_OF_YEAR)-1);
            calOpen.set(Calendar.DAY_OF_WEEK, 6);
            calClose.set(Calendar.WEEK_OF_YEAR, calClose.get(Calendar.WEEK_OF_YEAR)-1);
            calClose.set(Calendar.DAY_OF_WEEK, 6);
        } else if (dayOfWeek == 2 && today.get(Calendar.HOUR_OF_DAY) == 15 && today.get(Calendar.MINUTE) <= 30 || dayOfWeek == 2 && today.get(Calendar.HOUR_OF_DAY) < 15){
            System.out.println("dayofweek2 triggered");
            calOpen.set(Calendar.WEEK_OF_YEAR, calOpen.get(Calendar.WEEK_OF_YEAR)-1);
            calOpen.set(Calendar.DAY_OF_WEEK, 6);
            calClose.set(Calendar.WEEK_OF_YEAR, calClose.get(Calendar.WEEK_OF_YEAR)-1);
            calClose.set(Calendar.DAY_OF_WEEK, 6);
        } else if (today.get(Calendar.HOUR_OF_DAY) == 15 && today.get(Calendar.MINUTE) <= 30 || today.get(Calendar.HOUR_OF_DAY) < 15){
            System.out.println("dayofweek... triggered");
            calOpen.set(Calendar.DAY_OF_WEEK, calOpen.get(Calendar.DAY_OF_WEEK)-1);
            calClose.set(Calendar.DAY_OF_WEEK, calClose.get(Calendar.DAY_OF_WEEK)-1);
        }

        UTCTimeStamps.put("from", calOpen.getTime().getTime()/1000);
        UTCTimeStamps.put("to", calClose.getTime().getTime()/1000);
        //SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        //Date dateNow = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(dateMorning));

        return UTCTimeStamps;
    }
}

