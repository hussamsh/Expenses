package com.infinitetech.expenses.ActivityClasses;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.infinitetech.expenses.R;
import com.infinitetech.expenses.ExpenseAdapter;
import com.infinitetech.expenses.SqliteHandlers.ExpensesTableHandler;
import com.software.shell.fab.ActionButton;


public class SendActivity extends Activity implements AdapterView.OnItemSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ExpensesTableHandler handler = new ExpensesTableHandler(this);
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(handler.getExpensesOfTheDay(), this);
        recyclerView.setAdapter(expenseAdapter);

        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_send);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSuperToastAlert("sending");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void callSuperToastAlert(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FADE)).show();
    }
}
