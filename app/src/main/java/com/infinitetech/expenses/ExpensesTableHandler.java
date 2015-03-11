package com.infinitetech.expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;

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

    public void insertExpense(Expense expense){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(EXPENSES_COLUMN_NAME , expense.getName());
            cv.put(EXPENSES_COLUMN_COST, expense.getPrice());
            cv.put(EXPENSES_COLUMN_CATEGORY , expense.getCategory());
            cv.put(EXPENSES_COLUMN_DATE , expense.getDate());

            sqLiteDatabase.insert(TABLE_NAME , null , cv);
    }

    public ArrayList<Expense> getExpensesBetween(long start , long end) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Expense> expenses = new ArrayList<>();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where " + EXPENSES_COLUMN_DATE + " > " + start + " and " + EXPENSES_COLUMN_DATE + " < " + end, null);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_NAME));
            double price = c.getDouble(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_COST));
            String category = c.getString(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_CATEGORY));
            long date = c.getLong(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_DATE));
            expenses.add(new Expense(name , price , category , date));
        }
        c.close();
        db.close();
        return expenses ;
    }

    public ArrayList<Expense> getExpensesOfTheDay(){
        DateTime dateTime = new DateTime().withTimeAtStartOfDay();
        long startOfDay = dateTime.getMillis() ;
        long endOfDay = dateTime.plusDays(1).getMillis();
        return getExpensesBetween(startOfDay , endOfDay);
    }

    public Cursor getExpense(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME + " where id = " + id + " " , null);
       }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int)DatabaseUtils.queryNumEntries(db , TABLE_NAME);
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
