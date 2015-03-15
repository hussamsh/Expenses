package com.infinitetech.expenses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private String category ;

    private boolean wantsToExit;

    static FileOutputStream fileOutputStream ;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SignInActivity.getMoneyPreference() , Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("hasVisited" , false)){
            //startActivity(new Intent(this , SignInActivity.class));
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putBoolean("hasVisited" , true);
            e.apply();
        }

        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) , "Expenses.pdf");
        setTheView(file);

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (name.getText().toString().equals("") || cost.getText().toString().equals("")){
            callSuperToastAlert("Complete all fields");
        }else{
            Expense expense = new Expense(name.getText().toString() , Double.parseDouble(cost.getText().toString()) , category);
            handler.insertExpense(expense);
            SuperToast.create(this, "Saved", SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.GREEN ,SuperToast.Animations.SCALE)).show();
            name.setText("");
            cost.setText("");

        }


    }

    private void setTheView(final File file) {
        setTheSpinner();
        setProfilePicture();
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , ExpensesPdfFactory.class);
                MainActivity.this.sendBroadcast(intent);
                return true ;
            }
        });
        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendEmail(file);
                startActivity(new Intent(MainActivity.this , SendActivity.class));
            }
        });
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
        // The picture is from Facebook to get the google+ call mPerson.getImage().getUrl
        Picasso.with(this).load("https://lh5.googleusercontent.com/NIaQOHzHG5OeSRxYJ2tOYi1MC-hJQMuso9Sw7ygH_jU=s591").into(circularImageView);
        SharedPreferences sharedPreferences = getSharedPreferences(SignInActivity.getMoneyPreference() , Context.MODE_PRIVATE) ;
        String userName = sharedPreferences.getString("userName" , "Null");
        TextView textView = (TextView) findViewById(R.id.profile_textView);
        textView.setText(userName);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch ((int) parent.getItemIdAtPosition(position)) {
            case 0:
                category = "Food";
                break;
            case 1:
                category = "Household";
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
        SuperToast.create(this, Text, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FADE)).show();
    }

    private String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd\\MM\\yyyy");
        Date date1 = new Date();
        return simpleDateFormat.format(date1);
    }

    public static FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }

}
