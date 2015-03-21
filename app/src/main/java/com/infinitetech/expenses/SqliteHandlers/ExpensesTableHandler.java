package com.infinitetech.expenses.SqliteHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.infinitetech.expenses.TransactionTypes.Expense;
import com.infinitetech.expenses.enums.ExpenseCategory;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Hossam on 2/5/2015.
 */
public class ExpensesTableHandler extends TransactionTablesHandler{

    private static final String TABLE_NAME = "expenses";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";

    public ExpensesTableHandler(Context context){
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

    public void insertExpense(Expense expense){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_NAME, expense.getName());
            cv.put(COLUMN_AMOUNT, expense.getAmount());
            cv.put(COLUMN_CATEGORY, expense.getCategory().name());
            cv.put(COLUMN_DATE, expense.getDate());

            sqLiteDatabase.insert(TABLE_NAME , null , cv);
    }

    public Expense getExpense(long date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where date = " + date, null);
        String name = c.getString(c.getColumnIndex(ExpensesTableHandler.COLUMN_NAME));
        double price = c.getDouble(c.getColumnIndex(ExpensesTableHandler.COLUMN_AMOUNT));
        ExpenseCategory category = getExpenseCategory(c.getString(c.getColumnIndex(ExpensesTableHandler.COLUMN_CATEGORY)));
        long expenseDate = c.getLong(c.getColumnIndex(ExpensesTableHandler.COLUMN_DATE));
        c.close();
        db.close();
        return new Expense(name , price , category , expenseDate);
       }

    public void editExpense(String name , double price , String category , long date){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME , name);
        cv.put(COLUMN_AMOUNT, price);
        cv.put(COLUMN_CATEGORY , category);

        db.update(TABLE_NAME , cv , COLUMN_DATE + " = " + date ,null );
        db.close();
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int)DatabaseUtils.queryNumEntries(db , TABLE_NAME);
    }

    public ArrayList<Expense> getExpensesBetween(long startDate , long endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Expense> expenses = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_DATE + " > " + startDate + " and " + COLUMN_DATE + " < " + endDate, null, null, null, null);
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(COLUMN_NAME));
            double price = c.getDouble(c.getColumnIndex(COLUMN_AMOUNT));
            ExpenseCategory category = getExpenseCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
            long date = c.getLong(c.getColumnIndex(COLUMN_DATE));
            expenses.add(new Expense(name, price, category, date));
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

    public ArrayList<Expense> getExpensesOfTheWeek(){
        DateTime date = new DateTime();
        int dayOfTheWeek = Integer.parseInt(date.dayOfWeek().getAsString());
        date = date.withTimeAtStartOfDay();
        switch (dayOfTheWeek){
            case 7:
                return getExpensesBetween(date.getMillis(), date.plusDays(7).getMillis());
            default:
                return getExpensesBetween(date.minusDays((dayOfTheWeek)).getMillis(), date.plusDays((dayOfTheWeek * -1) + 7).getMillis());
        }
    }

    public ArrayList<Expense> getExpensesOfTheMonth(){
        DateTime date = new DateTime();
        switch (date.getMonthOfYear()){
            case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                return getExpensesBetween(date.withDayOfMonth(1).getMillis(), date.withDayOfMonth(31).getMillis());
            case 4:case 6:case 9:case 11:
                return getExpensesBetween(date.withDayOfMonth(1).getMillis(), date.withDayOfMonth(30).getMillis());
            case 2:
                return getExpensesBetween(date.withDayOfMonth(1).getMillis(), date.withDayOfMonth(28).getMillis());
            default:
                return null;
        }
    }


    //returns the total amount of money I spent between 2 dates
    public double getSpendingBetween(long startDate, long endDate){
       double total = 0 ;
        ArrayList<Expense> expenses = getExpensesBetween(startDate , endDate);
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    //Total amount of money for today
    public double getSpendingOfTheDay(){
        double total = 0 ;
        ArrayList<Expense> expenses = getExpensesOfTheDay();
        for(Expense expense : expenses){
            total += expense.getAmount();
        }
        return total;
    }

    // Total amount of money for a week
    public double getWeekSpending(){
        double total = 0 ;
        ArrayList<Expense> expenses = getExpensesOfTheWeek();
        for(Expense expense : expenses){
            total += expense.getAmount();
        }
        return total ;
    }

    //Total amount of money in a month
    public double getMonthSpending(){
        double total = 0 ;
        ArrayList<Expense> expenses = getExpensesOfTheMonth();
        for(Expense expense : expenses){
            total += expense.getAmount();
        }
        return total ;

    }

    private ExpenseCategory getExpenseCategory(String categoryString) {
        ExpenseCategory category;
        switch (categoryString) {
            case "Food":
                category = ExpenseCategory.Food;
                break;
            case "Household":
                category = ExpenseCategory.Household;
                break;
            case "Personal":
                category = ExpenseCategory.Personal;
                break;
            default:
                category = ExpenseCategory.Unknown;
        }
        return category;
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

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }

    public static String getColumnAmount() {
        return COLUMN_AMOUNT;
    }

    public static String getColumnCategory() {
        return COLUMN_CATEGORY;
    }

    public static String getColumnDate() {
        return COLUMN_DATE;
    }
}
