package com.onairm.recordtool4android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.utils.VideoCaptureHelper;

public class BiggerCoverActivity extends AppCompatActivity {
    private int pos;
    private String videoUrl;
    private ImageView ivCover;
    private FrameLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigger_cover);

        Intent intent=getIntent();
        if(null != intent){
            videoUrl=intent.getStringExtra("videoUrl");
            pos=intent.getIntExtra("pos",0);
        }

        ivCover=findViewById(R.id.ivCover);
        root=findViewById(R.id.root);

        Bitmap bitmap= VideoCaptureHelper.getVideoFrameImage(videoUrl,pos);

        ivCover.setImageBitmap(bitmap);

        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void jumpToBiggerCoverActivity(Context context,String videoUrl, int pos){
        Intent intent=new Intent(context,BiggerCoverActivity.class);
        intent.putExtra("videoUrl",videoUrl);
        intent.putExtra("pos",pos);

        context.startActivity(intent);
    }
}
