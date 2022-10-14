package com.aniketjain.chatappjava.Constant;

import android.app.ProgressDialog;
import android.content.Context;

public class ConstantProgressDialog {
    public static ProgressDialog progressDialog;
    Context context;

    public ConstantProgressDialog(Context context) {
        this.context = context;
        setProgressDialog(context);
    }

    public void setProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
    }

    public void show() {
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }
}
