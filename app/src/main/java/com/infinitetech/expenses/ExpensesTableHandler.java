package com.infinitetech.expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.DateTime;

/**
 * Created by Hossam on 2/5/2015.
 */
public class ExpensesTableHandler extends SQLiteOpenHelper{
    private static final String TAG = ExpensesTableHandler.class.getSimpleName();
    private static final String DATABASE_NAME = "Expenses.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "expenses";
    public static final String EXPENSES_COLUMN_ID = "id";
    public static final String EXPENSES_COLUMN_NAME = "name";
    public static final String EXPENSES_COLUMN_COST = "cost";
    public static final String EXPENSES_COLUMN_CATEGORY = "category";
    public static final String EXPENSES_COLUMN_DATE = "date";
    private boolean tableCreated ;
    ExpensesTableHandler(Context context){
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableCreated){
            db.execSQL("Create table expenses (" +
                    EXPENSES_COLUMN_ID +" integer primary key , " +
                    EXPENSES_COLUMN_NAME +" text not null, " +
                    EXPENSES_COLUMN_COST +" real not null, " +
                    EXPENSES_COLUMN_CATEGORY+" text not null, " +
                    EXPENSES_COLUMN_DATE+" integer not null )" );
            tableCreated = true ;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG , "Upgrading database from version " + oldVersion + " to " + newVersion +" which will destroy old data");
        db.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertExpense(String name , int cost , String category){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EXPENSES_COLUMN_NAME , name);
        cv.put(EXPENSES_COLUMN_COST, cost);
        cv.put(EXPENSES_COLUMN_CATEGORY , category);
        cv.put(EXPENSES_COLUMN_DATE , getUnixTimeStamp());

        sqLiteDatabase.insert(TABLE_NAME , null , cv);
        return true ;
    }

    public Cursor getExpensesOfTheDay(){
        SQLiteDatabase db = this.getReadableDatabase();
        DateTime dateTime = new DateTime().withTimeAtStartOfDay();
        long startOfDay = dateTime.getMillis() ;
        long endOfDay = dateTime.plusDays(1).getMillis();
        return db.rawQuery("select * from " + TABLE_NAME + " where " + EXPENSES_COLUMN_DATE + " > " + startOfDay + " and " + EXPENSES_COLUMN_DATE + " < " + endOfDay , null );
    }

    public Cursor getExpense(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME + " where id = " + id + " " , null);
       }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int)DatabaseUtils.queryNumEntries(db , TABLE_NAME);
    }

    public long getUnixTimeStamp(){
        return new DateTime().getMillis();
    }


    /*
    make an update expense method
    public boolean updateExpense(.....){

    }*/


    /*

    make a delete expense method
    public boolean deleteExpense(....){

    }
     */

}
