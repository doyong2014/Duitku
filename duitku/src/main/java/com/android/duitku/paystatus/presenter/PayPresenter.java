package com.android.duitku.paystatus.presenter;

import com.android.duitku.model.Pay;

/**
 * Created by latifalbar on 11/17/2015.
 */
public interface PayPresenter {
    void onInitView();

    void onProsesBayar(Pay pay, Boolean fromRedirect);
}
