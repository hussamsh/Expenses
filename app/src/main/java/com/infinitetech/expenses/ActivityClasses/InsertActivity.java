package com.infinitetech.expenses.ActivityClasses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.infinitetech.expenses.R;
import com.infinitetech.expenses.SqliteHandlers.ExpensesTableHandler;
import com.infinitetech.expenses.SqliteHandlers.IncomeTableHandler;
import com.infinitetech.expenses.TransactionTypes.Expense;
import com.infinitetech.expenses.TransactionTypes.Income;
import com.infinitetech.expenses.enums.ExpenseCategory;
import com.infinitetech.expenses.enums.MethodOfReceiving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    static FileOutputStream fileOutputStream ;

    private ExpenseCategory category ;

    private MethodOfReceiving methodOfReceiving ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        setSpinners();

        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) , "Expenses.pdf");

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void save(View v){
        ExpensesTableHandler expensesTableHandler = new ExpensesTableHandler(this);
        IncomeTableHandler incomeTableHandler = new IncomeTableHandler(this);
        EditText name = (EditText) findViewById(R.id.expense_name_edittext);
        EditText cost = (EditText) findViewById(R.id.expense_amount_editText);
        EditText income = (EditText) findViewById(R.id.income_edittext);
        if (name.getText().toString().equals("") && cost.getText().toString().equals("") && income.getText().toString().equals("")){
            callSuperToastNormal("Complete all fields");
        }else{
            if (!name.getText().toString().equals("") || !cost.getText().toString().equals("")){
                Expense expense = new Expense(name.getText().toString() , Double.parseDouble(cost.getText().toString()) , category);
                expensesTableHandler.insertExpense(expense);
                name.setText("");
                cost.setText("");
            }

            if (!income.getText().toString().equals("")){
                incomeTableHandler.insertIncome(new Income(Double.parseDouble(income.getText().toString()) , methodOfReceiving));
                income.setText("");
            }
            callSuperToastNormal("saved");
        }
    }


    private void setSpinners() {
        Spinner categorySpinner = (Spinner) findViewById(R.id.expense_spinner);
        Spinner incomeSpinner = (Spinner) findViewById(R.id.income_spinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.expenseSpinnerArray, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> incomeAdapter = ArrayAdapter.createFromResource(this,
                R.array.incomeSpinnerArray, android.R.layout.simple_spinner_item);
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        incomeSpinner.setAdapter(incomeAdapter);
        incomeSpinner.setOnItemSelectedListener(this);

    }


    public void sendEmail(File file){
        String[] to = {"hussamshhassan@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL , to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT , "Expenses of Day: " + getDate());
        emailIntent.putExtra(Intent.EXTRA_TEXT , "");
        emailIntent.putExtra(Intent.EXTRA_STREAM , Uri.fromFile(file));
        startActivity(Intent.createChooser(emailIntent, "Send Expenses email:..... "));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return true;
    }

    public static FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent ;
        if (spinner.getId() == R.id.expense_spinner){
            switch ((int)parent.getItemIdAtPosition(position)) {
                case 0:
                    category = ExpenseCategory.Food;
                    break;
                case 1:
                    category = ExpenseCategory.Household;
                    break;
                case 2:
                    category = ExpenseCategory.Personal;
                    break;
            }
        }else if (spinner.getId() == R.id.income_spinner){
            switch ((int) spinner.getItemIdAtPosition(position)){
                case 0:
                    methodOfReceiving = MethodOfReceiving.Maher_Mostafa;
                    break;
                case 1:
                    methodOfReceiving = MethodOfReceiving.Weird_transfer;
                    break;
                case 2:
                    methodOfReceiving = MethodOfReceiving.Western_union;
                    break;
                case 3:
                    methodOfReceiving = MethodOfReceiving.Loan;
            }
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        callSuperToastNormal("Select something");
    }

    private void callSuperToastNormal(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
    }

    private String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd\\MM\\yyyy");
        Date date1 = new Date();
        return simpleDateFormat.format(date1);
    }
}
