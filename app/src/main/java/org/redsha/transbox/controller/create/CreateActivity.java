package org.redsha.transbox.controller.create;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.redsha.transbox.R;
import org.redsha.transbox.bean.CreateBaseInfo;
import org.redsha.transbox.bean.CreateGetTimeInfo;
import org.redsha.transbox.bean.CreateOpenPsInfo;
import org.redsha.transbox.bean.CreateOpoInfo;
import org.redsha.transbox.bean.CreateOrganInfo;
import org.redsha.transbox.bean.CreateTransPersonInfo;
import org.redsha.transbox.bean.CreateTransToInfo;
import org.redsha.transbox.databinding.ActivityCreateBinding;
import org.redsha.transbox.engine.AppBaseActivity;
import org.redsha.transbox.util.PrefUtils;
import org.redsha.transbox.util.ToastUtil;

public class CreateActivity extends AppBaseActivity {

    private CreateData mData;
    private ActivityCreateBinding binding;
    public static boolean isWriteSample = false;
    Handler handler = new Handler();
    Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据

        }
    };
    @Override
    protected void initVariable() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create);
        mData = new CreateData();
        mData.setPageState(1);
        mData.setPageShow("下一步");

        // test
        CreateBaseInfo createBaseInfo = new CreateBaseInfo();
        createBaseInfo.setOrganCount("1");
        mData.setBaseInfo(createBaseInfo);
        CreateOrganInfo createOrganInfo = new CreateOrganInfo();
        createOrganInfo.setBloodSampleCount("1");
        createOrganInfo.setOrganizationSampleCount("1");
        mData.setOrgan(createOrganInfo);
        mData.setTime(new CreateGetTimeInfo());
        mData.setTo(new CreateTransToInfo());
        mData.setPerson(new CreateTransPersonInfo());
        mData.setOpo(new CreateOpoInfo());
        mData.setPs(new CreateOpenPsInfo());
//        mData.setCps(new CreateOpenPsConfirmInfo());
        binding.setInfo(mData);
        binding.setPresenter(new CreatePresenter(this, mData, binding));
        initEditEvent();


        //处理组织样本类型的写入逻辑
//
//        binding.idOrganSample.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s!=null&&s.toString().length()>0){
//                    binding.idOrganSample.setCursorVisible(false);
//                    binding.idOrganSample.setFocusableInTouchMode(false);
//                    ToastUtil.showToast(CreateActivity.isWriteSample+"set"+isSoftShowing());
//                }
//
//            }
//        });
//
    }
    //判断弹出软键盘
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }
    private void initEditEvent() {
        View v = binding.buttonClear1;
        v.setOnClickListener(mOnClickListener);

        v = binding.buttonClear2;
        v.setOnClickListener(mOnClickListener);


        EditText et = binding.etPs1;
        et.setOnFocusChangeListener(new LoginOnFocusChangeListener(
                R.id.button_clear1, true));

        et = binding.etPs2;
        et.setOnFocusChangeListener(new LoginOnFocusChangeListener(
                R.id.button_clear2, true));
    }

    public class LoginOnFocusChangeListener implements View.OnFocusChangeListener {

        private int mClearBtnId;
        private boolean mShowToast;

        public LoginOnFocusChangeListener(int clearBtnId, boolean showToast) {
            this.mClearBtnId = clearBtnId;
            this.mShowToast = showToast;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Button bt = (Button) findViewById(mClearBtnId);
            EditText et = (EditText) v;
            if (hasFocus && !TextUtils.isEmpty(et.getText().toString())) {
                bt.setVisibility(View.VISIBLE);
            } else {
                if (mShowToast && !hasFocus
                        && et.getText().toString().length() != 4) {
                    ToastUtil.showToast("密码必须4位哦！");
                }
                bt.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void initData() {
        /**
         * 初始化需要的的数据
         */
        mData.getBaseInfo().setDeviceType("android");
        mData.getOrgan().setDataType("new");
        mData.getPerson().setDataType("new");
        mData.getPerson().setTransferPersonid("");

        String boxid = PrefUtils.getString("boxid", "", getApplicationContext());
        String hospitalName = PrefUtils.getString("hospitalName", "", getApplicationContext());
        if (!TextUtils.isEmpty(boxid)) {
            mData.getBaseInfo().setBox_id(boxid);
        }
        if (!TextUtils.isEmpty(hospitalName)) {
            mData.getTo().setToHospName(hospitalName);
        }
    }

    public void nullToast() {
        ToastUtil.showToast(getString(R.string.common_nullToast));
    }

    public void phoneErrorToast() {
        ToastUtil.showToast(getString(R.string.export_phoneError));
    }

    public void nullOpoToast() {
        ToastUtil.showToast(getString(R.string.create_nullOpo));
    }

    public void psDiff() {
        ToastUtil.showToast(getString(R.string.create_ps_diff));
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_clear1: {
                    mData.getPs().setOpenPs1("");
                    break;
                }
                case R.id.button_clear2: {
                    mData.getPs().setOpenPs2("");
                    break;
                }
            }
        }
    };

}