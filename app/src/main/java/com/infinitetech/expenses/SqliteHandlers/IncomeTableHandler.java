package com.infinitetech.expenses.SqliteHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.infinitetech.expenses.TransactionTypes.Income;
import com.infinitetech.expenses.enums.MethodOfReceiving;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Hossam on 3/21/2015.
 */


public class IncomeTableHandler extends TransactionTablesHandler {
    private static final String TAG = IncomeTableHandler.class.getSimpleName();
    private boolean tableCreated ;
    private static final String TABLE_NAME = "income";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_METHOD_OF_RECEIVING = "method";
    private static final String COLUMN_DATE = "date";

    public IncomeTableHandler(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public void insertIncome(Income income){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_AMOUNT, income.getAmount());
        cv.put(COLUMN_DATE, income.getDate());
        cv.put(COLUMN_METHOD_OF_RECEIVING , income.getMethodOfReceiving().name());

        sqLiteDatabase.insert(TABLE_NAME , null , cv);
    }

    public void editIncome(double amount , MethodOfReceiving methodOfReceiving , long date ){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AMOUNT , amount);
        cv.put(COLUMN_METHOD_OF_RECEIVING, methodOfReceiving.name());

        db.update(TABLE_NAME, cv, COLUMN_DATE + " = " + date, null);
        db.close();
    }

    public Income getIncome(long date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_DATE + " = " + date, null);
        double amount = c.getDouble(c.getColumnIndex(COLUMN_AMOUNT));
        MethodOfReceiving methodOfReceiving = getMethodOfReceiving(c.getString(c.getColumnIndex(COLUMN_METHOD_OF_RECEIVING)));
        c.close();
        db.close();
        return new Income(amount , date , methodOfReceiving);
    }

    public ArrayList<Income> getIncomeBetween(long startDate , long endDate){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Income> incomes = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_DATE + " > " + startDate + " and " + COLUMN_DATE + " < " + endDate, null, null, null, null);
        while (c.moveToNext()) {
            double amount = c.getDouble(c.getColumnIndex(COLUMN_AMOUNT));
            MethodOfReceiving methodOfReceiving = getMethodOfReceiving(c.getString(c.getColumnIndex(COLUMN_METHOD_OF_RECEIVING)));
            long date = c.getLong(c.getColumnIndex(COLUMN_DATE));
            incomes.add(new Income(amount ,date , methodOfReceiving));
        }
        c.close();
        db.close();
        return incomes ;
    }

    public ArrayList<Income> getIncomeOfTheWeek(){
        DateTime date = new DateTime();
        int dayOfTheWeek = Integer.parseInt(date.dayOfWeek().getAsString());
        date = date.withTimeAtStartOfDay();
        switch (dayOfTheWeek){
            case 7:
                return getIncomeBetween(date.getMillis(), date.plusDays(7).getMillis());
            default:
                return getIncomeBetween(date.minusDays((dayOfTheWeek)).getMillis(), date.plusDays((dayOfTheWeek * -1) + 7).getMillis());
        }
    }

    public ArrayList<Income> getIncomeOfTheMonth(){
        DateTime date = new DateTime();
        switch (Integer.parseInt(date.monthOfYear().toString())){
            case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                return getIncomeBetween(date.withDayOfMonth(1).getMillis(), date.withDayOfMonth(31).getMillis());
            case 4:case 6:case 9:case 11:
                return getIncomeBetween(date.withDayOfMonth(1).getMillis(), date.withDayOfMonth(30).getMillis());
            case 2:
                return getIncomeBetween(date.withDayOfMonth(1).getMillis(), date.withDayOfMonth(29).getMillis());
            default:
                return null;
        }
    }

    public double getTotalIncomeInWeek(){
        double total = 0 ;
        ArrayList<Income> incomes = getIncomeOfTheWeek();
        for(Income income : incomes){
            total += income.getAmount();
        }
        return total ;
    }

    public double getTotalIncomeInMonth(){
        double total = 0 ;
        ArrayList<Income> incomes = getIncomeOfTheMonth();
        for(Income income : incomes){
            total += income.getAmount();
        }
        return total ;
    }

    private MethodOfReceiving getMethodOfReceiving(String methodString) {
        MethodOfReceiving method;
        switch (methodString) {
            case "Maher_Mostafa":
                method = MethodOfReceiving.Maher_Mostafa;
                break;
            case "Weird_transfer":
                method = MethodOfReceiving.Weird_transfer;
                break;
            case "Loan":
                method = MethodOfReceiving.Loan;
                break;
            case "Western_union":
                method = MethodOfReceiving.Western_union;
                break;
            default:
                method = MethodOfReceiving.Unknown;
        }
        return method;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnAmount() {
        return COLUMN_AMOUNT;
    }

    public static String getColumnMethodOfReceiving() {
        return COLUMN_METHOD_OF_RECEIVING;
    }

    public static String getColumnDate() {
        return COLUMN_DATE;
    }
}
