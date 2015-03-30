package com.infinitetech.expenses.ActivityClasses;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinitetech.expenses.ExpenseAdapter;
import com.infinitetech.expenses.R;
import com.infinitetech.expenses.SqliteHandlers.ExpensesTableHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends android.support.v4.app.Fragment {


    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ExpensesTableHandler handler = new ExpensesTableHandler(getActivity());
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(handler.getExpensesOfTheDay(), getActivity());
        recyclerView.setAdapter(expenseAdapter);

//        final ActionButton actionButton = (ActionButton) getView().findViewById(R.id.action_button_send);
//        actionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0){
//                    if (!actionButton.isHidden()){
//                        actionButton.hide();
//                    }
//                }else if (dy < 0){
//                    if (actionButton.isHidden()){
//                        actionButton.show();
//                    }
//                }
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses, container, false);
    }


}
