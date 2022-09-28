package com.maishuo.tingshuohenhaowan.auth;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.maishuo.tingshuohenhaowan.databinding.ViewFaceLivenessTimerOutLayoutBinding;
import com.qichuang.commonlibs.basic.BaseDialog;

import org.jetbrains.annotations.Nullable;

/**
 * 超时弹窗
 */
public class TimeoutDialog extends BaseDialog {

    private OnTimeoutDialogClickListener          mOnTimeoutDialogClickListener;
    private ViewFaceLivenessTimerOutLayoutBinding binding;

    public TimeoutDialog (@Nullable AppCompatActivity activity) {
        super(activity);
    }

    @Nullable
    @Override
    public View fetchRootView () {
        binding = ViewFaceLivenessTimerOutLayoutBinding.inflate(LayoutInflater.from(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void initWidgets () {
        binding.btnDialogRecollect.setOnClickListener(v -> {
            if (mOnTimeoutDialogClickListener != null) {
                mOnTimeoutDialogClickListener.onRecollect();
            }
        });
        binding.btnDialogReturn.setOnClickListener(v -> {
            if (mOnTimeoutDialogClickListener != null) {
                mOnTimeoutDialogClickListener.onReturn();
            }
        });
    }

    public void setDialogListener (OnTimeoutDialogClickListener listener) {
        mOnTimeoutDialogClickListener = listener;
    }

    public interface OnTimeoutDialogClickListener {

        void onRecollect ();

        void onReturn ();

    }
}
