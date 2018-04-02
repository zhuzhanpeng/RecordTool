package com.onairm.recordtool4android.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.base.BaseActivity;
import com.onairm.recordtool4android.player.JCVideoPlayerStandard;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bqy on 2018/1/25.
 */

public class VideoPublishActivity extends BaseActivity implements View.OnTouchListener{
    private TagFlowLayout pbTagLayout;
    private JCVideoPlayerStandard palyer;
    private EditText etName;
    private EditText etClass;
    private TextView tvMore;
    private EditText etIdea;
    private Button btnPublish;
    private ImageView ivBack;

    private TagAdapter<String> tagAdapter;
    private List<String> tagList;
    private String videoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_publish);

        Intent intent=getIntent();
        if(null != intent){
            videoUrl=intent.getStringExtra("videoUrl");
        }
        pbTagLayout = findViewById(R.id.pbTagLayout);
        palyer = findViewById(R.id.player);
        etName = findViewById(R.id.etName);
        etClass = findViewById(R.id.etClass);
        etIdea = findViewById(R.id.etIdea);
        tvMore = findViewById(R.id.tvMore);
        btnPublish = findViewById(R.id.btnPublish);
        ivBack = findViewById(R.id.ivBack);


        palyer.backButton.setVisibility(View.GONE);
        palyer.setUp(videoUrl, "");
        palyer.startButton.performClick();

        tagList = new ArrayList<>();
        tagList.add("hai hai hai hai");
        tagList.add("hai hai hai hai");
        tagList.add("hai hai hai hai");
        tagList.add("hai hai hai hai");
        tagList.add("hai hai hai hai");
        tagList.add("hai hai hai hai");
        tagList.add("hai hai hai hai");

        etIdea.setOnTouchListener(this);
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPublishActivity.this, TopicSearchActivity.class);
                startActivityForResult(intent, 2);

            }
        });

        tagAdapter = new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(VideoPublishActivity.this).inflate(R.layout.view_tag,
                        pbTagLayout, false);
                tv.setText(tagList.get(position));
                return tv;
            }
        };

        pbTagLayout.setAdapter(tagAdapter);
        tagAdapter.setSelected(0, tagList.get(0));
        tagAdapter.setSelectedList(0);

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishDialog();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    String tag = data.getStringExtra("topic");
                    tagList.set(0, tag);
                    tagAdapter.notifyDataChanged();
                    tagAdapter.setSelected(0, tagList.get(0));
                    tagAdapter.setSelectedList(0);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.etIdea && canVerticalScroll(etIdea))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText 需要判断的EditText
     * @return true：可以滚动  false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    private void publishDialog() {
        final Dialog dialog = new Dialog(this, R.style.DialogStyle);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_publish_success);
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public static void jumpToVideoPublishActivity(Context context,String videoUrl){
        Intent intent = new Intent(context,VideoPublishActivity.class);
        intent.putExtra("videoUrl",videoUrl);
        context.startActivity(intent);
    }
}

