package com.ecbclass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ecbclass.database.MainActivity;
import com.ecbclass.database.NewPostActivity;
import com.ecbclass.database.PostDetailActivity;

public class InternetConnector_Receiver extends BroadcastReceiver {
    public InternetConnector_Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            boolean isVisible = NetworkCheker.isActivityVisible();// Check if
            // activity
            // is
            // visible
            // or not
            Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

            // If it is visible then trigger the task else do nothing
            if (isVisible == true) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();

                // Check internet connection and accrding to state change the
                // text of activity by calling method
                if (networkInfo != null && networkInfo.isConnected()) {
                    new NewPostActivity().changeStatus(true);
                    new PostDetailActivity().changeStatus(true);
                    new MainActivity().changeStatus(true);
                } else {
                    new NewPostActivity().changeStatus(false);
                    new PostDetailActivity().changeStatus(false);
                    new MainActivity().changeStatus(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
