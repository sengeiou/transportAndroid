package com.otqc.transbox.controller.mixture;

import android.text.TextUtils;

import com.otqc.transbox.databinding.ActivityExportBinding;
import com.otqc.transbox.http.HttpHelper;
import com.otqc.transbox.http.HttpObserver;
import com.otqc.transbox.http.response.ExportBean;
import com.otqc.transbox.util.Validator;

public class ExportPresenter {
    private ExportActivity mActivity;
    private ActivityExportBinding mBinding;

    private String mExId;

    public ExportPresenter(ExportActivity exportActivity, String exId, ActivityExportBinding binding) {
        this.mActivity = exportActivity;
        this.mExId = exId;
        this.mBinding = binding;
    }

    public void back() {
        mActivity.finish();
    }

    public void export(final ExportData info) {
        mBinding.sendBtn.setEnabled(false);
        mBinding.sendBtn.setText("加载中...");

        boolean checkPhone = checkPhone(info.getPhone());
        if (checkPhone) {
            /**
             * 发送请求服务器成功后 给出提示
             */
            new HttpHelper().exportPhone(info.getPhone(), mExId).
                    subscribe(new HttpObserver<ExportBean>() {
                        @Override
                        public void onComplete() {
                            mBinding.sendBtn.setEnabled(true);
                            mBinding.sendBtn.setText("发送");
                        }

                        @Override
                        public void onSuccess(ExportBean model) {

                            mActivity.showDlg();
                        }
                    });

        } else {
            mBinding.sendBtn.setEnabled(true);
            mBinding.sendBtn.setText("发送");
        }

    }

    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mActivity.phoneNullToast();
            return false;
        }

        if (!Validator.isMobile(phone)) {
            mActivity.phoneErrorToast();
            return false;
        }

        return true;
    }

}
