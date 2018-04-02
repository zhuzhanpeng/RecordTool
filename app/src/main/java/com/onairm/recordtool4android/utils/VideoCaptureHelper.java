package com.onairm.recordtool4android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.onairm.baselibrary.Init;
import com.onairm.baselibrary.utils.TipToast;
import com.onairm.recordtool4android.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 视频截取工具类
 */

public class VideoCaptureHelper {
    private volatile static VideoCaptureHelper INSTANCE;
    private FFmpeg ffmpeg;
    private Activity activity;

    
    //构造方法私有
    private VideoCaptureHelper() {
        ffmpeg=FFmpeg.getInstance(Init.context);
        loadFFMpegBinary();
    }

    //获取单例
    public static VideoCaptureHelper getInstance(Activity activity) {
        if (INSTANCE == null) {
            synchronized (VideoCaptureHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VideoCaptureHelper();
                }
            }
        }
        INSTANCE.activity=activity;
        return INSTANCE;
    }
    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    if(null != captureProgressListener){
                        captureProgressListener.onFailure(s);
                    }
                }

                @Override
                public void onSuccess(String s) {
                    if(null != captureProgressListener){
                        captureProgressListener.onSuccess(s);
                    }
                }

                @Override
                public void onProgress(String s) {
                    if(null != captureProgressListener){
                        captureProgressListener.onProgress(s);
                    }
                }

                @Override
                public void onStart() {
                    if(null != captureProgressListener){
                        captureProgressListener.onStart();
                    }
                }

                @Override
                public void onFinish() {
                    if(null != captureProgressListener){
                        captureProgressListener.onFinish();
                    }
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
            e.printStackTrace();
        }
    }

    // TODO: 2018/1/22 不好又不知道如何该
    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(INSTANCE.activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(Init.context.getString(R.string.device_not_supported))
                .setMessage(Init.context.getString(R.string.device_not_supported_message))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        INSTANCE.activity.finish();
                    }
                })
                .create()
                .show();
    }

    /**
     * @param startTime 00:00:00
     * @param duration  00:00:30
     * @param srcPath 原来的视频绝对地址
     * @param destPath 视频片段的保存地址
     * */
    public void captureVideo(String startTime,String duration,String srcPath,String destPath
            ,CaptureProgressListener captureProgressListener){
        this.captureProgressListener=captureProgressListener;
        if("0".equals(duration)||"00".equals(duration)||"00:00".equals(duration)||"00:00:00".equals(duration)){
            TipToast.shortTip("视频时长不能为0");
            return;
        }
        if(TextUtils.isEmpty(srcPath)||TextUtils.isEmpty(destPath)){
            TipToast.shortTip("原视频地址或目标视频地址不正确");
            return;
        }
        String str="-y -ss "+startTime+" -t "+duration+" -i "+srcPath+" -vcodec copy -acodec copy "+destPath;


        String[] command = str.split(" ");
        if(command.length!=0){
            execFFmpegBinary(command);
        }
    }
    private CaptureProgressListener captureProgressListener;
    public interface CaptureProgressListener{
        void onFailure(String s);
        public void onSuccess(String s);
        public void onProgress(String s);
        public void onStart();
        public void onFinish();
    }
    public static Bitmap getVideoFrameImage(String videoUrl,int pos){
        MediaMetadataRetriever mediaRetriever = new MediaMetadataRetriever();
        mediaRetriever.setDataSource(videoUrl);
        String vTime = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int interValSec = Integer.valueOf(vTime) / Constants.THUMB_COUNT;
        Bitmap bitmap = mediaRetriever.getFrameAtTime(pos * interValSec * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        return bitmap;
    }
    public static void saveBitmap(Bitmap bm,String picPath) {

        File f = new File(picPath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
