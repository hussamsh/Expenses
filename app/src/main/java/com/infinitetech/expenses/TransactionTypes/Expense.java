package com.infinitetech.expenses.TransactionTypes;

import com.infinitetech.expenses.enums.ExpenseCategory;

/**
 * Created by Hossam on 3/11/2015.
 */
public class Expense extends MoneyTransaction {

    private String name ;
    ExpenseCategory category ;

    public Expense(String name, double amount, ExpenseCategory category) {
        super(amount);
        this.name = name;
        this.category = category;
    }

    public Expense( String name , double amount , ExpenseCategory category , long date) {
        super(amount, date);
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public ExpenseCategory  getCategory() {
        return category;
    }

}
