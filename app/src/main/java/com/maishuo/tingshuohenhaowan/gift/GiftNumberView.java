package com.maishuo.tingshuohenhaowan.gift;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maishuo.tingshuohenhaowan.R;

/**
 * author ：yh
 * date : 2021/1/20 16:39
 * description : 礼物右侧礼物数量的展示
 */
public class GiftNumberView extends LinearLayout {

    private ImageView mNumberFirst;
    private ImageView mNumberSecond;
    private ImageView mNumberThird;
    private ImageView mNumberFour;
    private ImageView mNumberFive;

    public GiftNumberView (Context context) {
        this(context, null);
    }

    public GiftNumberView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftNumberView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init (Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.gift_number_item, this);
        mNumberFirst = view.findViewById(R.id.iv_gift_number_first);
        mNumberSecond = view.findViewById(R.id.iv_gift_number_second);
        mNumberThird = view.findViewById(R.id.iv_gift_number_third);
        mNumberFour = view.findViewById(R.id.iv_gift_number_four);
        mNumberFive = view.findViewById(R.id.iv_gift_number_five);
    }

    /**
     * 设置数量的显示
     */
    public void setNumber (int number) {
        int firstNumber  = 1;
        int secondNumber = 0;
        int thirdNumber  = 0;
        int fourNumber   = 0;
        int fiveNumber   = 0;
        if (number > 0 && number < 10) {
            firstNumber = number;
            mNumberFirst.setImageResource(getNumberResource(firstNumber));
        } else if (number > 9 && number < 100) {
            firstNumber = number / 10;
            secondNumber = number % 10;
            mNumberSecond.setVisibility(VISIBLE);
            mNumberFirst.setImageResource(getNumberResource(firstNumber));
            mNumberSecond.setImageResource(getNumberResource(secondNumber));
        } else if (number > 99 && number < 1000) {
            firstNumber = number / 100;
            secondNumber = (number % 100) / 10;
            thirdNumber = (number % 100) % 10;
            mNumberSecond.setVisibility(VISIBLE);
            mNumberThird.setVisibility(VISIBLE);
            mNumberFirst.setImageResource(getNumberResource(firstNumber));
            mNumberSecond.setImageResource(getNumberResource(secondNumber));
            mNumberThird.setImageResource(getNumberResource(thirdNumber));

        } else if (number > 999 && number < 10000) {
            firstNumber = number / 1000;
            secondNumber = (number % 1000) / 100;
            thirdNumber = ((number % 1000) % 100) / 10;
            fourNumber = ((number % 1000) % 100) % 10;
            mNumberSecond.setVisibility(VISIBLE);
            mNumberThird.setVisibility(VISIBLE);
            mNumberFour.setVisibility(VISIBLE);
            mNumberFirst.setImageResource(getNumberResource(firstNumber));
            mNumberSecond.setImageResource(getNumberResource(secondNumber));
            mNumberThird.setImageResource(getNumberResource(thirdNumber));
            mNumberFour.setImageResource(getNumberResource(fourNumber));

        } else if (number > 9999) {
            firstNumber = number / 10000;
            secondNumber = (number % 10000) / 1000;
            thirdNumber = ((number % 10000) % 1000) / 100;
            fourNumber = (((number % 10000) % 1000) % 100) / 10;
            fiveNumber = (((number % 10000) % 1000) % 100) % 10;
            mNumberSecond.setVisibility(VISIBLE);
            mNumberThird.setVisibility(VISIBLE);
            mNumberFour.setVisibility(VISIBLE);
            mNumberFive.setVisibility(VISIBLE);
            mNumberFirst.setImageResource(getNumberResource(firstNumber));
            mNumberSecond.setImageResource(getNumberResource(secondNumber));
            mNumberThird.setImageResource(getNumberResource(thirdNumber));
            mNumberFour.setImageResource(getNumberResource(fourNumber));
            mNumberFive.setImageResource(getNumberResource(fiveNumber));
        }
    }

    /**
     * 获取数字对应的资源文件
     */
    private int getNumberResource (int number) {
        switch (number) {
            case 0:
                return R.mipmap.gift_number_0;
            case 1:
                return R.mipmap.gift_number_1;
            case 2:
                return R.mipmap.gift_number_2;
            case 3:
                return R.mipmap.gift_number_3;
            case 4:
                return R.mipmap.gift_number_4;
            case 5:
                return R.mipmap.gift_number_5;
            case 6:
                return R.mipmap.gift_number_6;
            case 7:
                return R.mipmap.gift_number_7;
            case 8:
                return R.mipmap.gift_number_8;
            case 9:
                return R.mipmap.gift_number_9;
            default:
                break;
        }

        return R.mipmap.gift_number_1;
    }
}
