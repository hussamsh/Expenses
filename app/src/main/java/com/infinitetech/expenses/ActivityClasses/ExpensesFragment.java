package com.infinitetech.expenses.ActivityClasses;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinitetech.expenses.ExpenseAdapter;
import com.infinitetech.expenses.ExpensesPdfFactory;
import com.infinitetech.expenses.R;
import com.infinitetech.expenses.SqliteHandlers.ExpensesTableHandler;
import com.software.shell.fab.ActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends android.support.v4.app.Fragment {


    public ExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ExpensesTableHandler handler = new ExpensesTableHandler(getActivity());
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(handler.getExpensesOfTheDay(), getActivity());
        recyclerView.setAdapter(expenseAdapter);



        final ActionButton actionButton = (ActionButton) getView().findViewById(R.id.action_button_send);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , ExpensesPdfFactory.class);
                getActivity().sendBroadcast(intent);
                sendEmail();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    if (!actionButton.isHidden()){
                        actionButton.hide();
                    }
                }else if (dy < 0){
                    if (actionButton.isHidden()){
                        actionButton.show();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses, container, false);
    }

    public void sendEmail(){
        String[] to = {"sherifsm@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL , to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT , "Expenses of Day: " + getDate());
        emailIntent.putExtra(Intent.EXTRA_TEXT , "");
        emailIntent.putExtra(Intent.EXTRA_STREAM , Uri.fromFile(ExpensesPdfFactory.getFile()));
        startActivity(Intent.createChooser(emailIntent, "Send Expenses email:..... "));
    }

    private String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd\\MM\\yyyy");
        Date date1 = new Date();
        return simpleDateFormat.format(date1);
    }


}
