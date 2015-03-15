package com.infinitetech.expenses;

import org.joda.time.DateTime;

/**
 * Created by Hossam on 3/11/2015.
 */
public class Expense {

    private String name ;
    private double price ;
    private String category ;
    private long date ;

    public Expense(String name) {
        this.name = name;
    }

    public Expense(String name, double price, String category, long date) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.date = date;
    }

    public Expense(String name, double price, String category) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = getUnixTimeStamp();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public long getDate() {
        return date;
    }

    public String getFormattedDate(){
        DateTime dateTime = new DateTime(getDate());
        String formatted = "";
        int hours = dateTime.getHourOfDay();
        String minutes = dateTime.getMinuteOfHour() < 10 ?"0"+ dateTime.getMinuteOfHour() : dateTime.getMinuteOfHour()+"" ;
        if ( hours > 12){
            formatted  += String.valueOf(hours - 12) + ":" + minutes + " PM";
        }else {
            formatted += hours + ":" + minutes + " AM";
        }
        return formatted;
    }

    private long getUnixTimeStamp(){
        return new DateTime().getMillis();
    }
}
