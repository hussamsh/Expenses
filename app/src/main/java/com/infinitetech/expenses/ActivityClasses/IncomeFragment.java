package com.infinitetech.expenses.ActivityClasses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinitetech.expenses.R;
import com.infinitetech.expenses.SqliteHandlers.IncomeTableHandler;
import com.infinitetech.expenses.TransactionTypes.Income;

public class IncomeFragment extends android.support.v4.app.Fragment {


    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        IncomeTableHandler handler = new IncomeTableHandler(getActivity());
        Income latest = handler.getLatestIncome();
        TextView incomeAmount = (TextView) getView().findViewById(R.id.latest_income_text);
        TextView incomeMethod = (TextView) getView().findViewById(R.id.income_methodOfReceiving_text);
        TextView incomeDate = (TextView) getView().findViewById(R.id.income_date_text);
        incomeAmount.setText(latest.getAmount()+"");
        incomeDate.setText(latest.getFormattedDate());
        incomeMethod.setText(latest.getMethodOfReceiving().toString());

    }
}
