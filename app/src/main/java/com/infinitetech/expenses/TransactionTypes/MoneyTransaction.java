package com.infinitetech.expenses.TransactionTypes;

import org.joda.time.DateTime;

/**
 * Created by Hossam on 3/21/2015.
 */
public abstract class MoneyTransaction {

    private double amount ;
    private long date ;

    protected MoneyTransaction(double amount) {
        this.amount = amount;
        this.date = new DateTime().getMillis();
    }

    protected MoneyTransaction(double amount, long date) {
        this.amount = amount;
        this.date = date;
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

    public double getAmount() {
        return amount;
    }

    public long getDate() {
        return date;
    }
}
