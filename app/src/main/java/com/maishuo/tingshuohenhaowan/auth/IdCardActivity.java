package com.maishuo.tingshuohenhaowan.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionSettingPage;
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil;
import com.qichuang.commonlibs.basic.IBasicActivity;
import com.qichuang.commonlibs.utils.ToastUtil;


public class IdCardActivity extends IBasicActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CAMERA = 800;

    private ImageView nextBtn;
    private EditText  usernameEt;
    private EditText  idcardEt;

    private String username = "";
    private String idNumber = "";

    public static void toIdCardActivity (Context context) {
        Intent intent = new Intent(context, IdCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_layout);

        findView();
        addListener();
        requestPermission();
    }

    private void checkPermission () {
        PermissionUtil.checkPermission(
                IdCardActivity.this,
                Manifest.permission.CAMERA,
                new PermissionUtil.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission () {

                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown (String... permission) {
                        PermissionUtil.requestPermission(
                                IdCardActivity.this,
                                Manifest.permission.CAMERA,
                                PERMISSIONS_REQUEST_CAMERA
                        );
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk (String... permission) {
                        jumpSettingActivity();
                    }
                });
    }

    private void jumpSettingActivity () {
        DialogUtils.showCommonCustomDialog(
                this,
                getString(R.string.not_apply_permission_tip_title),
                getString(R.string.not_apply_permission_tip_message),
                new OnDialogBackListener() {
                    @Override
                    public void onSure (String content) {
                        PermissionSettingPage.start(IdCardActivity.this);
                    }

                    @Override
                    public void onCancel () {
                        finish();
                    }
                }
        );
    }

    private void requestPermission () {
        if (!PermissionUtil.checkPermission(this, Manifest.permission.CAMERA)) {
            DialogUtils.showCommonCustomDialog(this,
                    getString(R.string.real_user_auth_tip_title),
                    getString(R.string.real_user_auth_tip_message),
                    new OnDialogBackListener() {
                        @Override
                        public void onSure (String content) {
                            checkPermission();
                        }

                        @Override
                        public void onCancel () {
                            finish();
                        }
                    });
        }
    }

    private void findView () {
        nextBtn = findViewById(R.id.img_next);
        ImageView backBtn = findViewById(R.id.iv_login_back);
        usernameEt = findViewById(R.id.username_et);
        idcardEt = findViewById(R.id.idnumber_et);
        backBtn.setOnClickListener(v -> finish());
    }

    private void addListener () {
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        if (v == nextBtn) {
            if (TextUtils.isEmpty(usernameEt.getText())) {
                ToastUtil.showToast("请输入姓名");
                return;
            }

            if (TextUtils.isEmpty(idcardEt.getText())) {
                ToastUtil.showToast("请输入身份证");
                return;
            }

            username = usernameEt.getText().toString();
            idNumber = idcardEt.getText().toString();

            PermissionUtil.checkPermission(
                    this,
                    Manifest.permission.CAMERA,
                    new PermissionUtil.PermissionCheckCallBack() {
                        @Override
                        public void onHasPermission () {
                            jumpToOnlineVerify();
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDown (String... permission) {
                            requestPermission();
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDownAndDontAsk (String... permission) {
                            jumpSettingActivity();
                        }
                    });
        }
    }

    // 身份证识别成功后跳转到人脸离线活体检测
    private void jumpToOnlineVerify () {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(IdCardActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(idNumber)) {
            Toast.makeText(IdCardActivity.this, "身份证不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 调转到活体识别界面
        Intent faceIntent = new Intent(IdCardActivity.this, FaceOnlineVerifyActivity.class);
        faceIntent.putExtra("username", username);
        faceIntent.putExtra("idnumber", idNumber);
        startActivity(faceIntent);
        finish();
    }
}
