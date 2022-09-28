package com.maishuo.tingshuohenhaowan.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.maishuo.tingshuohenhaowan.R;
import com.qichuang.commonlibs.basic.IBasicActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 在线检测活体和公安核实
 */

public class FaceOnlineVerifyActivity extends IBasicActivity implements View.OnClickListener {

    public static final int OFFLINE_FACE_LIVENESS_REQUEST = 100;

    private String username;
    private String idnumber;
    private Button retBtn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_online_check);

        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            idnumber = intent.getStringExtra("idnumber");
        }

        retBtn = findViewById(R.id.retry_btn);
        retBtn.setOnClickListener(this);

        // 打开离线活体检测
        Intent faceLivenessintent = new Intent(this, FaceLivenessExpActivity.class);
        startActivityForResult(faceLivenessintent, OFFLINE_FACE_LIVENESS_REQUEST);
    }

    @Override
    public void onClick (View v) {
        if (v == retBtn) {
            // 打开离线活体检测
            Intent faceLivenessintent = new Intent(this, FaceLivenessExpActivity.class);
            startActivityForResult(faceLivenessintent, OFFLINE_FACE_LIVENESS_REQUEST);
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OFFLINE_FACE_LIVENESS_REQUEST && data != null) {
            String filePath = data.getStringExtra("bestimage_path");
            if (TextUtils.isEmpty(filePath)) {
                EventBus.getDefault().post(new BaiduAuthBean(null, null, null, "认证图片未找到"));
                finish();
            } else {
                Message msg = new Message();
                msg.what = 0x1001;
                msg.obj = filePath;
                handler.sendMessageDelayed(msg, 200);
            }
        } else {
            finish();
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage (@NonNull Message msg) {
            super.handleMessage(msg);
            if (0x1001 == msg.what && msg.obj instanceof String) {
                String filePath = (String) msg.obj;
                EventBus.getDefault().post(new BaiduAuthBean(
                        username,
                        idnumber,
                        filePath,
                        null));
            }
            finish();
        }
    };

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(0x1001);
        }
    }

}

