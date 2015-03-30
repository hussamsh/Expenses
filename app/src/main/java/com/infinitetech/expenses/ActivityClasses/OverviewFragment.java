package com.infinitetech.expenses.ActivityClasses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinitetech.expenses.R;
import com.infinitetech.expenses.SqliteHandlers.ExpensesTableHandler;
import com.infinitetech.expenses.SqliteHandlers.IncomeTableHandler;
import com.software.shell.fab.ActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewFragment#} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ActionButton button = (ActionButton) getView().findViewById(R.id.action_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity() , InsertActivity.class));
            }
        });
        button.show();
        TextView todayExpenses = (TextView) getView().findViewById(R.id.total_expenses_today_amount_text);
        TextView weekIncome = (TextView) getView().findViewById(R.id.income_week_amount_text);
        TextView weekExpenses = (TextView) getView().findViewById(R.id.total_expenses_week_amount_text);
        TextView remainingMoney = (TextView) getView().findViewById(R.id.remaining_amount_text);

        ExpensesTableHandler expensesTableHandler = new ExpensesTableHandler(getActivity());
        IncomeTableHandler incomeTableHandler = new IncomeTableHandler(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ExpensesApp", Context.MODE_PRIVATE);

        todayExpenses.setText(expensesTableHandler.getSpendingOfTheDay()+"");
        weekIncome.setText(incomeTableHandler.getTotalIncomeInWeek()+"");
        weekExpenses.setText(expensesTableHandler.getWeekSpending()+"");
        remainingMoney.setText(sharedPreferences.getString("remaining", "0.0"));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }


}
