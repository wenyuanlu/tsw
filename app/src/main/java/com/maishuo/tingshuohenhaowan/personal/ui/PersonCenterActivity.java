package com.maishuo.tingshuohenhaowan.personal.ui;

import android.content.Context;
import android.content.Intent;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;

/**
 * Create by yun on 2021/2/28
 * EXPLAIN:
 */
public class PersonCenterActivity extends CustomBaseActivity {

    public static void to(Context context, String userId) {
        to(context, -1, userId);
    }

    public static void to(Context context, int position, String userId) {
        Intent intent = new Intent(context, PersonCenterActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_center;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Intent intent   = getIntent();
        String userId   = intent.getStringExtra("userId");
        int    position = intent.getIntExtra("position", -1);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_person,
                        new PersonalFragment(position, userId))
                .commit();
    }
}
