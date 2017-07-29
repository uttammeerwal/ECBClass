package com.ecbclass.database;

import android.app.ProgressDialog;

import com.ecbclass.user_activity.Home;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends Home {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
