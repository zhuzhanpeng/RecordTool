package com.onairm.recordtool4android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onairm.baselibrary.utils.TipToast;
import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.adapter.PosterAdapter;
import com.onairm.recordtool4android.adapter.TimeSelectAdapter;
import com.onairm.recordtool4android.base.BaseActivity;
import com.onairm.recordtool4android.base.BaseRVAdapter;
import com.onairm.recordtool4android.player.JCUtils;
import com.onairm.recordtool4android.player.JCVideoPlayer;
import com.onairm.recordtool4android.player.JCVideoPlayerStandard;
import com.onairm.recordtool4android.utils.Constants;
import com.onairm.recordtool4android.utils.VideoCaptureHelper;
import com.onairm.recordtool4android.view.VideoSeekBar;

public class VideoCaptureActivity extends BaseActivity implements View.OnClickListener {

    private JCVideoPlayerStandard playerStandard;
    private String srcPath;
    /**
     * 视频剪辑View
     */
    public static VideoSeekBar videoSeekBar;

    private String destPath = Constants.CLIP_AFTER_VIDEO_PATH;
    protected ProgressDialog dialog;

    private ImageView ivBack;
    private TextView btnSave;
    private RecyclerView timeSlectRecyView;
    private RecyclerView selectPosterRecyView;
    private MediaPlayer mediaPlayer;

    private TimeSelectAdapter timeSelectAdapter;
    private PosterAdapter posterAdapter;

    private int startTime, endTime;//单位：毫秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_capture);
        initData();
        findViews();
        initViews();
        addListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            srcPath = intent.getStringExtra("srcPath");
        }
        startTime = 5 * 1000;
        endTime = startTime + 5 * 1000;
    }

    private void findViews() {
        playerStandard = findViewById(R.id.player);
        videoSeekBar = findViewById(R.id.video_seekbar);
        dialog = new ProgressDialog(VideoCaptureActivity.this);
        dialog.setCancelable(false);
        ivBack = findViewById(R.id.ivBack);
        timeSlectRecyView = findViewById(R.id.timeSlectRecyView);
        selectPosterRecyView = findViewById(R.id.selectPosterRecyView);
        btnSave = findViewById(R.id.btnSave);
    }

    private void initViews() {
        if (null != dialog) {
            dialog.setMessage("正在加载缩略图");
            dialog.show();
        }
        if (!TextUtils.isEmpty(srcPath)) {
            playerStandard.setUp(srcPath, "");
            playerStandard.startButton.performClick();
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        timeSlectRecyView.setLayoutManager(layoutManager);
        timeSelectAdapter = new TimeSelectAdapter();
        timeSlectRecyView.setAdapter(timeSelectAdapter);

        // ====== 视频剪辑View  ======
        // 进行重置
        videoSeekBar.reset();
//		// 是否需要绘制进度 - 白色进度动,以及走过的画面背景变暗 - 统一控制setProgressLine(isDrawProgress), setProgressBG(isDrawProgress)
//		am_video_seekbar.setProgressDraw(isDrawProgress);
//		//// 是否需要绘制进度 - 播放中,有个白色的线条在动
//		am_video_seekbar.setProgressLine(isDrawProgressLine);
//		// 是否需要绘制进度 - 播放过的画面背景变暗
//		am_video_seekbar.setProgressBG(isDrawProgressBG);
//		// 是否属于裁剪模式 - 两边有进度滑动
//		am_video_seekbar.setCutMode(isCutMode);
//		// 是否属于裁剪模式 - 是否绘制非裁剪模块变暗
//		am_video_seekbar.setCutMode(isCutMode, isDrawProgressLine);
        // 视频关键帧间隔(毫秒,表示左右两个模块最低限度滑动时间,无法选择低于该关键帧的裁剪时间)
        float videoFrame = 60 * 1000f;
        // 设置本地视频路径 - 默认裁剪模式,则不绘制播放背景
//        videoSeekBar.setVideoUri(true, srcPath, videoFrame);
//		// 不设置关键帧时间,则默认最多是两个ImageView左右多出的宽度
        videoSeekBar.setVideoUri(true, srcPath, 5);
    }

    private void notifySeekTo(int millSecs) {
        if (null != mediaPlayer) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(millSecs);
            }
        }
    }

    private void addListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        playerStandard.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                mediaPlayer = mp;

            }
        });
        videoSeekBar.setScrollListener(new VideoSeekBar.ScrollListener() {
            @Override
            public void leftScrollStartTime(int leftMillSecs) {
                notifySeekTo(leftMillSecs);
                startTime = leftMillSecs;
                timeSelectAdapter.setSelectIndex(-1);
                timeSelectAdapter.notifyDataSetChanged();
            }

            @Override
            public void centerScrollStartTime(int leftMillSecs, int rightMillSecs) {
                notifySeekTo(leftMillSecs);
                startTime = leftMillSecs;
                endTime = rightMillSecs;
            }

            @Override
            public void rightScrollEndTime(int rightMillSecs) {
                notifySeekTo(rightMillSecs);
                endTime = rightMillSecs;
                timeSelectAdapter.setSelectIndex(-1);
                timeSelectAdapter.notifyDataSetChanged();
            }
        });
        timeSelectAdapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handleTimeSelect(position);
            }
        });

        videoSeekBar.setThumbFinishListener(new VideoSeekBar.ThumbFinish() {
            @Override
            public void finish(Bitmap[] bitmaps) {
                GridLayoutManager layoutManager = new GridLayoutManager(VideoCaptureActivity.this, Constants.THUMB_COUNT);
                selectPosterRecyView.setLayoutManager(layoutManager);
                posterAdapter = new PosterAdapter(bitmaps);
                posterAdapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        handlePosterSelect(position);
                    }
                });
                posterAdapter.setOnLongClickListener(new BaseRVAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View view, int position) {
                        BiggerCoverActivity.jumpToBiggerCoverActivity(VideoCaptureActivity.this
                                , srcPath, position);
                    }
                });
                selectPosterRecyView.setAdapter(posterAdapter);
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //条件过滤
                VideoCaptureHelper.getInstance(VideoCaptureActivity.this)
                        .captureVideo(JCUtils.stringForTime(startTime)
                                , JCUtils.stringForTime(endTime - startTime)
                                , srcPath, destPath
                                , new VideoCaptureHelper.CaptureProgressListener() {
                                    @Override
                                    public void onFailure(String s) {
                                        //出错
                                       // Log.e("fail", "onFailure: " + s);
                                        TipToast.shortTip("截取出错");
                                    }

                                    @Override
                                    public void onSuccess(String s) {
                                        //更新播放器地址
                                        /*if(!TextUtils.isEmpty(destPath)){
                                            playerStandard.setUp(destPath,"");
                                            playerStandard.startButton.performClick();
                                        }*/
//                                        TipToast.shortTip("视频截取成功");

                                    }

                                    @Override
                                    public void onProgress(String s) {

                                    }

                                    @Override
                                    public void onStart() {
                                        //开始
                                        if(null != dialog){
                                            dialog.setMessage("正在保存");
                                            dialog.show();
                                        }
                                    }

                                    @Override
                                    public void onFinish() {
                                        //完成
                                        Bitmap bitmap = VideoCaptureHelper.getVideoFrameImage(srcPath, posterAdapter.getSelectIndex());
                                        VideoCaptureHelper.saveBitmap(bitmap, Constants.BIG_IMG_PATH);
//                                        TipToast.shortTip("封面图保存成功");
                                        //跳转到发布界面
                                        VideoPublishActivity
                                                .jumpToVideoPublishActivity(VideoCaptureActivity.this
                                                        , destPath);
                                        if (null != dialog) {
                                            dialog.dismiss();
                                        }
                                    }
                                });

            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoSeekBar.destroy();
    }

    private void handleTimeSelect(int position) {
        timeSelectAdapter.setSelectIndex(position);
        videoSeekBar.setUpProgress((position + 1) * 5);
        timeSelectAdapter.notifyDataSetChanged();
    }

    private void handlePosterSelect(int position) {
        posterAdapter.setSelectIndex(position);
        posterAdapter.notifyDataSetChanged();
    }

    public static void jumpToVideoCaptureActivity(Context context, String srcPath) {
        Intent intent = new Intent(context, VideoCaptureActivity.class);
        intent.putExtra("srcPath", srcPath);
        context.startActivity(intent);
    }
}
