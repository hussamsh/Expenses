package com.infinitetech.expenses.ActivityClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.infinitetech.expenses.R;


public class SignInActivity extends Activity implements  GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = SignInActivity.class.getSimpleName();

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;


    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean wantsToExit;

    TextView textView;

    SignInButton signInButton;

    Person mPerson ;

    private SuperActivityToast loadingToast;

    private static final String MONEY_PREFERENCE = "money_sharedPreference";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /* Initializing the shared preference */
        sharedPreferences = getSharedPreferences(MONEY_PREFERENCE , Context.MODE_PRIVATE);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        if (isNetworkAvailable()){
                signInButton = (SignInButton) findViewById(R.id.sign_in_button);
                textView = (TextView) findViewById(R.id.welcomeTextView);
                signInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callSuperToastLoad("Connecting to Google");
                        mGoogleApiClient.connect();
                    }
                });
        }else{
            callSuperToastAlert("No network is available");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
    public void onConnected(Bundle bundle) {
        mPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        loadingToast.dismiss();
        callSuperToastNormal(mPerson.getDisplayName() + " fetched successfully");
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString("userName" , mPerson.getDisplayName());
        e.apply();
        startActivity(new Intent(SignInActivity.this , MainActivity.class));
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        callSuperToastAlert("Some Error");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIntentInProgress && connectionResult.hasResolution()){
            try {
                mIntentInProgress =  true ;
                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),RC_SIGN_IN ,null , 0 , 0 , 0);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false ;
                mGoogleApiClient.connect();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN)
            mIntentInProgress = false ;
        if (!mGoogleApiClient.isConnecting())
            mGoogleApiClient.connect();
    }

    // Super Toast methods for simplicity

    private void callSuperToastNormal(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
    }

    private void callSuperToastAlert(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
    }

    private void callSuperToastLoad(String Text){
        loadingToast = new SuperActivityToast(this , SuperToast.Type.PROGRESS);
        loadingToast.setText(Text);
        loadingToast.setIndeterminate(true);
        loadingToast.setProgressIndeterminate(true);
        loadingToast.setBackground(SuperToast.Background.PURPLE);
        loadingToast.setTextSize(SuperToast.TextSize.SMALL);
        loadingToast.show();
    }

    public static String getMoneyPreference() {
        return MONEY_PREFERENCE;
    }
}
