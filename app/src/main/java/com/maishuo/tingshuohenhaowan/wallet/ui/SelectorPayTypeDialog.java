package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;


/**
 * dialog 有两个按钮
 * Created by yindy on 2017/1/16.
 */
public class SelectorPayTypeDialog extends Dialog {

    private TextView payTip;
    private RelativeLayout linearAliPay;
    private RelativeLayout linearWecatPay;

    private String               titleText;
    private String               messageText = "选择支付方式";
    private DialogActionListener mActionListener;

    public void setActionListener (DialogActionListener mActionListener) {
        this.mActionListener = mActionListener;
    }

    public SelectorPayTypeDialog (Context context) {
        super(context, R.style.DialogTheme);
    }

    public SelectorPayTypeDialog (Context context, String titleText, String messageText) {
        super(context, R.style.DialogTheme);
        this.titleText = titleText;
        this.messageText = messageText;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_select_pay_layout);
        payTip=findViewById(R.id.pay_tip);
        linearAliPay=findViewById(R.id.linear_alipay);
        linearWecatPay=findViewById(R.id.linear_wecat_pay);
        if (!TextUtils.isEmpty(titleText)) {
            payTip.setText(titleText);
        }
        payTip.setText(messageText);

        linearAliPay.setOnClickListener(view -> {
            if (mActionListener != null) {
                mActionListener.toAli();
            }
        });
        linearWecatPay.setOnClickListener(view -> {
            if (mActionListener != null) {
                mActionListener.toWeCat();
            }
        });
    }

    private void initWindow () {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
        window.setAttributes(attributes);
    }

    @Override
    public void show () {
        if (!isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss () {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public interface DialogActionListener {

        void toAli ();

        void toWeCat ();

    }
}
