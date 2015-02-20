package com.infinitetech.expenses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private String category ;

    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheSpinner();
        CircleImageView circularImageView = (CircleImageView) findViewById(R.id.profile_image);
        Picasso.with(this).load("https://lh5.googleusercontent.com/NIaQOHzHG5OeSRxYJ2tOYi1MC-hJQMuso9Sw7ygH_jU=s591").into(circularImageView);
        sharedPreferences = getSharedPreferences(SignInActivity.MONEY_PREFERENCE , Context.MODE_PRIVATE) ;
        String userName = sharedPreferences.getString("userName" , "Null");
        TextView textView = (TextView) findViewById(R.id.profile_textView);
        textView.setText(userName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void save(View v){
        ExpensesTableHandler handler = new ExpensesTableHandler(this);
        EditText name = (EditText) findViewById(R.id.expense_editText);
        EditText cost = (EditText) findViewById(R.id.expense_amount_editText);
        handler.insertExpense(name.getText().toString() , Integer.parseInt(cost.getText().toString()) ,
                category );
        SuperToast.create(this, "Saved", SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.GREEN ,SuperToast.Animations.SCALE)).show();

    }

    private void setTheSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerArray ,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch ((int) parent.getItemIdAtPosition(position)) {
            case 0:
                category = "Food";
                break;
            case 1:
                category = "HouseHold";
                break;
            case 2:
                category = "Personal";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        callSuperToastAlert("Select something");
    }

    private long getUnixTime(){
        return System.currentTimeMillis()/1000L ;
    }

    // Super Toast methods for simplicity
    private void callSuperToast(String Text ,int duration ){
        SuperToast.create(this, Text, duration, Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
    }

    private void callSuperToastNormal(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
    }

    private void callSuperToastAlert(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
    }


}
