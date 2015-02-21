package com.infinitetech.expenses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Hossam on 2/21/2015.
 */

public class EmailSender extends BroadcastReceiver{

    private String TAG = EmailSender.class.getSimpleName();
     Context context ;

    public EmailSender() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sendMailTask sendMailTask = new sendMailTask();
        sendMailTask.execute();
    }

    private String getTable(){
            ExpensesTableHandler handler = new ExpensesTableHandler(context);
            DateTimeFormatter builder = DateTimeFormat.forPattern("dd\\MM\\YYYY");
            String table =
                         builder.print(new DateTime().getMillis() / 1001L) + "\n" +
                "********************************************************************************** \n" +
                "|       Name          ||       Price      ||        Category      ||     Time|     \n" +
                "********************************************************************************** \n";

        Cursor cursor = handler.getExpensesOfTheDay();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_COST));
            String category = cursor.getString(cursor.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_CATEGORY));
            String date = getFormattedDate(cursor.getLong(cursor.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_DATE)));
            table = table.concat(buildRow(name , price , category , date));
        }
        return table ;
    }

    private String getFormattedDate(long unixTime){
        DateTime dateTime = new DateTime(unixTime);
        String formatted = "";
        int hours = dateTime.getHourOfDay();
        String minutes = dateTime.getMinuteOfDay() > 10 ?"0"+ dateTime.getMinuteOfHour() : dateTime.getMinuteOfHour()+"" ;
        if ( hours > 12){
            formatted  += String.valueOf(hours - 12) + ":" + minutes + " PM";
        }else {
            formatted += hours + ":" + minutes + " AM";
        }
        return formatted;
    }

    private String  buildRow(String name , double Price , String category , String time) {

        return "       " + name + "       --        " + Price + "    --        " + category + "     --     " + time + "       \n";

    }

    public class sendMailTask extends AsyncTask<Void , Void , Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            GmailHelper gmailHelper = new GmailHelper("hussamshhassan@gmail.com" , "hos@35766");
            try {
                DateTimeFormatter builder = DateTimeFormat.forPattern("dd\\MM\\YYYY");
                gmailHelper.sendMail("Expenses from: " + builder.print(System.currentTimeMillis() / 1000L) , getTable() , "hussamshhassan@gmail.com" , "hosronaldo1@gmail.com");
                return true ;
            } catch (Exception e) {
                e.printStackTrace();
                return false ;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            callSuperToastAlert("Expenses sent", context );
        }
    }

    private void callSuperToastAlert(String Text , Context context) {
        SuperToast.create(context, Text, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
    }
}
