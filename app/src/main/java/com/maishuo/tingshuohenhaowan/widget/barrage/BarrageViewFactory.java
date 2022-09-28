package com.maishuo.tingshuohenhaowan.widget.barrage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.maishuo.tingshuohenhaowan.R;

/**
 * author ：Seven
 * date : 2021/6/3
 * description : 创建BarrageView
 */
class BarrageViewFactory {
    @SuppressLint("InflateParams")
    static BarrageView createBarrageView(Context context) {
        return (BarrageView) LayoutInflater.from(context)
                .inflate(R.layout.view_barrage, null, false);
    }

    static BarrageView createBarrageView(Context context, ViewGroup parent) {
        return (BarrageView) LayoutInflater.from(context)
                .inflate(R.layout.view_barrage, parent, false);
    }
}
