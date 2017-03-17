package com.android.duitku.redirectbankpage.presenter;

import com.android.duitku.redirectbankpage.view.RedirectBankPageView;

/**
 * Created by latif on 12/6/15.
 */
public class RedirectWebPagePresenterImpl implements RedirectBankPagePresenter {

    private RedirectBankPageView mRedirectBankPageView;
    public RedirectWebPagePresenterImpl(RedirectBankPageView redirectBankPageView) {
        this.mRedirectBankPageView = redirectBankPageView;
    }

    @Override
    public void onInitView() {
        mRedirectBankPageView.initView();
    }

    @Override
    public void onLoadBankWebPage(String bankUrl) {
        mRedirectBankPageView.loadBankWebPage(bankUrl);
    }
}
