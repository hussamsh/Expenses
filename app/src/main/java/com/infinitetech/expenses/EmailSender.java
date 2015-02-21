package com.infinitetech.expenses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

/**
 * Created by Hossam on 2/21/2015.
 */

public class EmailSender extends BroadcastReceiver{

    private String TAG = EmailSender.class.getSimpleName();
    SuperActivityToast superActivityToast  ;

    public EmailSender() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //superActivityToast = new SuperActivityToast((android.app.Activity) context);
        Log.i(TAG , "Started counting");
        callSuperToastAlert("Your time us up", context);

    }


    public class sendMailTask extends AsyncTask<Void , Void , Boolean> {

        @Override
        protected void onPreExecute() {
            callSuperToastLoad("Sending Mail");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            GmailHelper gmailHelper = new GmailHelper("hussamshhassan@gmail.com" , "hos@35766");
            try {
                gmailHelper.sendMail("Hello from android" , "Well this was sent from android cheers" , "hussamshhassan@gmail.com" , "hosronaldo1@gmail.com");
                return true ;
            } catch (Exception e) {
                e.printStackTrace();
                return false ;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            superActivityToast.dismiss();
        }
    }

    private void callSuperToastLoad(String Text){
        superActivityToast.setText(Text);
        superActivityToast.setIndeterminate(true);
        superActivityToast.setProgressIndeterminate(true);
        superActivityToast.setBackground(SuperToast.Background.PURPLE);
        superActivityToast.setTextSize(SuperToast.TextSize.SMALL);
        superActivityToast.show();
    }

    private void callSuperToastAlert(String Text , Context context) {
        SuperToast.create(context, Text, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
    }
}
