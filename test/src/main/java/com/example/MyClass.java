package com.example;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MyClass {


    public static void main(String[] args) {
        DateTime dateTime = new DateTime();
        long unixDate = dateTime.getMillis();
        String formatted = "";
        DateTimeFormatter builder = DateTimeFormat.forPattern("dd\\MM\\YYYY");
        formatted += builder.print(unixDate);
        int hours = dateTime.getHourOfDay();
        if ( hours > 12){
            formatted  += " " + String.valueOf(hours - 12) + ":" + dateTime.getMinuteOfHour() + " PM";
        }else {
            formatted += " " + hours + ":" + dateTime.getMinuteOfHour() + " AM";
        }

        System.out.println(formatted);
    }
}
