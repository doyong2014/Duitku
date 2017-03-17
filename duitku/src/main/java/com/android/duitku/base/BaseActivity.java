package com.android.duitku.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.duitku.R;
import com.android.duitku.helper.NetworkActivity;
import com.android.duitku.model.Login;
import com.android.duitku.utils.DuitkuPreferences;

/**
 * Created by latifalbar on 11/12/2015.
 */
public class BaseActivity extends NetworkActivity {
    private Toolbar mToolbar;

    private android.support.v7.app.AlertDialog mAlert;

    private ProgressDialog progressDialog;

    private DuitkuPreferences duitkuPreferences;

    public void initToolbarView() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
    }

    public void showSimpleDialog(String title, String message, String btnTitle, boolean cancelable, DialogInterface.OnClickListener listner){
        if(btnTitle == null)
            btnTitle = "Close";

        dismissSimpleDialog();

        if( !this.isFinishing())
            mAlert = new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage(message==null?"":message)
                    .setCancelable(cancelable)
                    .setPositiveButton(btnTitle, listner)
                    .setTitle(title).show();

    }

    public void dismissSimpleDialog(){
        if(mAlert != null && mAlert.isShowing())
            mAlert.dismiss();
    }

    public void showSimpleProgressDialog(String message){
        if(progressDialog !=null && progressDialog.isShowing())
            progressDialog.dismiss();
        if(message == null)
            message = "Loading...";

        if( !this.isFinishing())
            progressDialog = ProgressDialog.show(this, null, message, true, false);
    }

    public void dismissSimpleProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void clearPreferences(Context context){
        duitkuPreferences = new DuitkuPreferences(context);
        duitkuPreferences.saveLogin("");
        duitkuPreferences.saveLoginResponse("");
        duitkuPreferences.savePayResponse("");
        duitkuPreferences.setUsingDuitku(false);
        duitkuPreferences.saveImageName("");
        duitkuPreferences.saveAdminFee(0);
        duitkuPreferences.saveInquiryResponse("");
        duitkuPreferences.saveInquiry("");
        duitkuPreferences.setIsLastPaymentFailed(false);
    }
}
