package com.onairm.recordtool4android.activity;


import android.annotation.SuppressLint;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.onairm.baselibrary.utils.TipToast;
import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.base.BaseActivity;
import com.onairm.recordtool4android.fragment.MyVideoFragment;
import com.onairm.recordtool4android.fragment.RecordFragment;
import com.onairm.recordtool4android.utils.SdcardUtils;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private List<Fragment> fragments;
    private RadioButton tabOne;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments = new ArrayList<>();
        fragments.clear();

        Log.d("sdcard","sdcard>>>>>>>>>>>>>>>>"+ SdcardUtils.hasSpace(MainActivity.this));

        initViews();
        initFragment();
        initListener();
        switchFragment(0);

    }

    private void initFragment() {
        RecordFragment recordFragment = RecordFragment.newInstance();
        MyVideoFragment myVideoFragment = MyVideoFragment.newInstance();
        fragments.add(recordFragment);
        fragments.add(myVideoFragment);
    }

    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    private void initViews() {
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        tabOne = (RadioButton) findViewById(R.id.tabOne);
        tabOne.setChecked(true);
    }

    public void switchFragment(int position) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i == position) {
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.fContainer, fragment);
                }
            } else {
                if (fragment.isAdded()) {
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.tabOne:
                switchFragment(0);
                break;
            case R.id.tabTwo:
                switchFragment(1);
                break;
        }
    }
}
