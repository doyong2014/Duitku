package com.android.duitku.inquiry.presenter;
import com.android.duitku.inquiry.view.InquiryActivityView;

/**
 * Created by latifalbar on 11/12/2015.
 */
public class InquiryPresenterImpl implements InquiryPresenter {

    private InquiryActivityView mInquiryActivityView;

    public InquiryPresenterImpl(InquiryActivityView mainActivityView) {
        this.mInquiryActivityView = mainActivityView;
    }

    @Override
    public void onInitView() {
        mInquiryActivityView.initView();
    }


    @Override
    public void onPopulateSpinner() {
        mInquiryActivityView.populateSpinner();
    }


}
