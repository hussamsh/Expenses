package com.infinitetech.expenses.TransactionTypes;

import com.infinitetech.expenses.enums.MethodOfReceiving;

/**
 * Created by Hossam on 3/21/2015.
 */
public class Income extends MoneyTransaction{

    MethodOfReceiving methodOfReceiving;

    public Income(double amount , MethodOfReceiving methodOfReceiving) {
        super(amount);
        this.methodOfReceiving = methodOfReceiving;
    }

    public Income(double amount, long date, MethodOfReceiving methodOfReceiving) {
        super(amount, date);
        this.methodOfReceiving = methodOfReceiving;
    }

    public MethodOfReceiving getMethodOfReceiving() {
        return methodOfReceiving;
    }

}
