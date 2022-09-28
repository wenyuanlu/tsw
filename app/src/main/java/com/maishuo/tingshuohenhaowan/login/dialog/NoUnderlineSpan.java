package com.maishuo.tingshuohenhaowan.login.dialog;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * 无下划线的Span
 */
public class NoUnderlineSpan extends UnderlineSpan {

    @Override
    public void updateDrawState (TextPaint ds) {
        ds.setColor(Color.parseColor("#FF42A1"));
        ds.setUnderlineText(false);
    }
}