package com.infinitetech.expenses;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private String category ;

    private boolean wantsToExit;

    SharedPreferences sharedPreferences ;

    SuperActivityToast superActivityToast ;

    ExpensesTableHandler handler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheView();
        Button button = (Button) findViewById(R.id.send_button);
        Intent intent = new Intent(this  , EmailSender.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this , 0 , intent , 0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP , System.currentTimeMillis() + (3 * 1000) , pendingIntent);

            }
        });

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
        handler = new ExpensesTableHandler(this);
        EditText name = (EditText) findViewById(R.id.expense_editText);
        EditText cost = (EditText) findViewById(R.id.expense_amount_editText);
        handler.insertExpense(name.getText().toString() , Integer.parseInt(cost.getText().toString()) ,
                category );
        SuperToast.create(this, "Saved", SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.GREEN ,SuperToast.Animations.SCALE)).show();
        name.setText("");
        cost.setText("");


    }

    private void setTheView() {
        setTheSpinner();
        setProfilePicture();

    }


    private void setTheSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerArray ,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setProfilePicture() {
        CircleImageView circularImageView = (CircleImageView) findViewById(R.id.profile_image);
        Picasso.with(this).load("https://lh5.googleusercontent.com/NIaQOHzHG5OeSRxYJ2tOYi1MC-hJQMuso9Sw7ygH_jU=s591").into(circularImageView);
        sharedPreferences = getSharedPreferences(SignInActivity.MONEY_PREFERENCE , Context.MODE_PRIVATE) ;
        String userName = sharedPreferences.getString("userName" , "Null");
        TextView textView = (TextView) findViewById(R.id.profile_textView);
        textView.setText(userName);
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

    @Override
    public void onBackPressed() {
        if (!wantsToExit){
            wantsToExit = true ;
            callSuperToastAlert("Press back again to Exit");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        wantsToExit = false ;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            super.onBackPressed();
        }
    }

    private void callSuperToastNormal(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
    }

    private void callSuperToastAlert(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
    }

    private void callSuperToastLoad(String Text){
        superActivityToast = new SuperActivityToast(this , SuperToast.Type.PROGRESS);
        superActivityToast.setText(Text);
        superActivityToast.setIndeterminate(true);
        superActivityToast.setProgressIndeterminate(true);
        superActivityToast.setBackground(SuperToast.Background.PURPLE);
        superActivityToast.setTextSize(SuperToast.TextSize.SMALL);
        superActivityToast.show();
    }




}
