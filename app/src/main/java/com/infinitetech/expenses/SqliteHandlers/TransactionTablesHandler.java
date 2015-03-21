package com.infinitetech.expenses.SqliteHandlers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hossam on 3/21/2015.
 */
public abstract class TransactionTablesHandler extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Expenses.db";
    private static final int DATABASE_VERSION = 1;
    private boolean tableCreated ;


    public TransactionTablesHandler(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableCreated){

            db.execSQL("Create table " + ExpensesTableHandler.getTableName() + " ( " +
                    ExpensesTableHandler.getColumnName() +" text not null, " +
                    ExpensesTableHandler.getColumnAmount() +" real not null, " +
                    ExpensesTableHandler.getColumnCategory() +" text not null, " +
                    ExpensesTableHandler.getColumnDate() +" integer primary key not null )" );

            db.execSQL("Create table "+  IncomeTableHandler.getTableName()  + "  ( " +
                    IncomeTableHandler.getColumnDate() +" integer primary key not null, " +
                    IncomeTableHandler.getColumnAmount() +" real not null, " +
                    IncomeTableHandler.getColumnMethodOfReceiving() + " text not null )"  );

            tableCreated = true ;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + ExpensesTableHandler.getTableName());
        db.execSQL("Drop table if exists " + IncomeTableHandler.getTableName());
        onCreate(db);
    }
}
