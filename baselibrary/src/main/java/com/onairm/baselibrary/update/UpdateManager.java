package com.onairm.baselibrary.update;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onairm.baselibrary.utils.TipToast;
import com.onairm.baselibrary.utils.Utils;
import com.onairm.tv.baselibrary.R;
import com.yanzhikai.pictureprogressbar.PictureProgressBar;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;


/**
 * Created by android on 2017/3/1.
 * <p>
 * 调用方式：
 * UpdateManager updateManager = new UpdateManager(context);
 * updateManager.setVersionInfo(versionInfo);
 * updateManager.checkUpdate(DownAppDialog dialog);
 */

public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    //    Basis basis = SharePreferences.getLocalBasis();
    private VersionInfo versionInfo;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private PictureProgressBar pictureProgressBar;
    private TextView tvTitle;
    private AlertDialog mDownloadDialog;
    private String apkName = "update.apk";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    if (mProgress != null) {
                        mProgress.setProgress(progress);
                    }
                    if (pictureProgressBar != null) {
                        pictureProgressBar.setProgress(progress);
                        tvTitle.setText("新版本下载中… " + progress + "%");
                    }
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     * <p>
     * 需要传入提示窗口布局
     */
    public void checkUpdate(DownAppDialog dialog) {
        if (isUpdate()) {
            // 显示提示对话框
           /* if (MainApplication.getInstance().isAleartDown()) {*/
            showNoticeDialog(dialog);
              /*  MainApplication.getInstance().setAleartDown(false);
            }*/
        } else {

        }
    }

    /**
     * 检测软件更新
     * <p>
     * 需要传入提示窗口布局
     */
    public void checkUpdate(DownAppDialog dialog, DownProgressDialog downProgressDialog) {
        if (isUpdate()) {
            // 显示提示对话框
           /* if (MainApplication.getInstance().isAleartDown()) {*/
            showNoticeDialog(dialog, downProgressDialog);
              /*  MainApplication.getInstance().setAleartDown(false);
            }*/
        } else {

        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
        if (versionInfo != null) {
            String netVersion = versionInfo.getVersion();
            String version = Utils.getVersion();
            if (compareVersion(netVersion, version)) {
                return true;
            }
        }
        return false;
    }

    private boolean compareVersion(String netVersion, String localVersion) {
        String[] arr1 = netVersion.split("\\.");
        String[] arr2 = localVersion.split("\\.");
        for (int i = 0; i < arr1.length; i++) {
            if (i >= arr2.length) {
                return false;
            }
            if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
                return true;
            }
            if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
                return false;
            }
        }
        return false;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(final DownAppDialog dialog) {
        dialog.setDescribe(versionInfo.getDescription());
        dialog.setNegativeClickListener(new DownAppDialog.NegativeClickListener() {
            @Override
            public void negativeClick(TextView view) {
                if (versionInfo.getMustUpdate() == 1) {
                    System.exit(0);
                } else if (versionInfo.getMustUpdate() == 0) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setPositiveClickListener(new DownAppDialog.PositiveClickListener() {
            @Override
            public void positiveClick(TextView view) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        dialog.setNegativeGoneListener(new DownAppDialog.NegativeGoneListener() {
            @Override
            public boolean negativeGone() {
                if (versionInfo.getMustUpdate() == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(final DownAppDialog dialog, final DownProgressDialog progress) {
        dialog.setDescribe(versionInfo.getDescription());
        dialog.setTitle("新版本 "+versionInfo.getVersion());
        dialog.setNegativeClickListener(new DownAppDialog.NegativeClickListener() {
            @Override
            public void negativeClick(TextView view) {
                if (versionInfo.getMustUpdate() == 1) {
                    System.exit(0);
                } else if (versionInfo.getMustUpdate() == 0) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setPositiveClickListener(new DownAppDialog.PositiveClickListener() {
            @Override
            public void positiveClick(TextView view) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog(progress);
            }
        });
        dialog.setNegativeGoneListener(new DownAppDialog.NegativeGoneListener() {
            @Override
            public boolean negativeGone() {
                if (versionInfo.getMustUpdate() == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
//        // 取消更新
//        builder.setNegativeButton(R.string.soft_update_cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                // 设置取消状态
//                cancelUpdate = true;
//            }
//        });
        mDownloadDialog = builder.create();
//        mDownloadDialog.getButton(DialogInterface.BUTTON_POSITIVE).setFocusable(true);
        mDownloadDialog.show();
        mDownloadDialog.setCancelable(false);
        // 现在文件
        downloadApk();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog(DownProgressDialog dialog) {
        dialog.create();
        dialog.show();
        pictureProgressBar = dialog.getProgressBar();
        tvTitle = dialog.getTvTitle();
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    public void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, apkName);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(versionInfo.getUrl());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, apkName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            mDownloadDialog.dismiss();
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                TipToast.shortTip("MalformedURLException");
            } catch (UnknownHostException e) {
                // do something
//                TipToast.shortTip("UnknownHostException");
                TipToast.shortTip("网络连接失败，请稍后重试");
                mDownloadDialog.dismiss();
            } catch (ConnectTimeoutException e) {
                e.printStackTrace();
//                TipToast.shortTip("ConnectTimeoutException");
                TipToast.shortTip("网络连接失败，请稍后重试");
                mDownloadDialog.dismiss();
            } catch (SocketException e) {
                // do something
//                TipToast.shortTip("SocketException");
                TipToast.shortTip("网络连接失败，请稍后重试");
                mDownloadDialog.dismiss();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                TipToast.shortTip("SocketTimeoutException");
                TipToast.shortTip("网络连接失败，请稍后重试");
                mDownloadDialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
//                TipToast.shortTip("IOException");
            }
//            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }
}
