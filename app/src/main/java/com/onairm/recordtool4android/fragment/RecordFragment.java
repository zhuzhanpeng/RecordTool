package com.onairm.recordtool4android.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.activity.VideoCaptureActivity;
import com.onairm.recordtool4android.adapter.CategoryAdapter;
import com.onairm.recordtool4android.base.BaseFragment;
import com.onairm.recordtool4android.bean.CategoryDto;
import com.onairm.recordtool4android.services.RecordService;
import com.onairm.recordtool4android.utils.AppDataUtils;
import com.onairm.recordtool4android.utils.Constants;
import com.onairm.recordtool4android.utils.MeiZuPermissionUtil;
import com.onairm.recordtool4android.view.PGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;

@RuntimePermissions
public class RecordFragment extends BaseFragment {
    private static final int RECORD_REQUEST_CODE = 101;
    private MediaProjectionManager projectionManager;
    private RecordService recordService;
    private MediaProjection mediaProjection;
    private Button startRecord;
    private int flag = 0;
    private PGridView rGridView;
    private List<CategoryDto> categoryDtoList;
    private CategoryAdapter categoryAdapter;

    private Timer timer;
    private TextView textTime;
    private TimerTask timerTask;

    private WindowManager windowManager;
    private TextView floatTextView;
    private WindowManager.LayoutParams params;
    private int statusBarHeight;
    private TextView startText;
    private TextView recordCompelte;

    public static RecordFragment newInstance() {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectionManager = (MediaProjectionManager) getActivity().getSystemService(MEDIA_PROJECTION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getStatuHeight();
        initViews(view);
        categoryDtoList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryDtoList, getActivity());
        rGridView.setAdapter(categoryAdapter);
        getFloatView();
        getData();
        Intent intent = new Intent(getActivity(), RecordService.class);
        getActivity().bindService(intent, connection, BIND_AUTO_CREATE);

        startRecord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (recordService.isRunning()) {
                    recordService.stopRecord();
                    startText.setText(R.string.start_record);
                    startRecord.setBackground(getResources().getDrawable(R.drawable.ic_home_record_start));
                    recordCompelte.setVisibility(View.VISIBLE);
                    stopTime(); //停止计时
                    editRecordVideo(); // 视频编辑
                } else {
                    flag = 2;
                    RecordFragmentPermissionsDispatcher.toVideoCaptureActivityWithPermissionCheck(RecordFragment.this);
                }
            }
        });
        initLister();
    }

    private void editRecordVideo() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recordCompelte.setVisibility(View.GONE);
                flag = 1;
                RecordFragmentPermissionsDispatcher.toVideoCaptureActivityWithPermissionCheck(RecordFragment.this);
            }
        }, 3000);
    }

    private void getData() {
        List<CategoryDto> data = AppDataUtils.getData();
        categoryDtoList.addAll(data);
        categoryAdapter.notifyDataSetChanged();
    }

    private void initViews(View view) {
        textTime = (TextView) view.findViewById(R.id.textTime);
        timer = new Timer();
        startText = (TextView) view.findViewById(R.id.startText);
        startRecord = (Button) view.findViewById(R.id.startRecord);
        startRecord.setEnabled(false);
        rGridView = (PGridView) view.findViewById(R.id.rGridView);
        recordCompelte = (TextView) view.findViewById(R.id.recordCompelte);
    }

    private void getFloatView() {
        floatTextView = new TextView(getActivity());
        floatTextView.setGravity(Gravity.END);
        floatTextView.setBackgroundColor(Color.RED);
        floatTextView.setText("录制中....");
        floatTextView.setTextSize(12);
        floatTextView.setTextColor(Color.BLACK);
        //类型是TYPE_TOAST,普通的Android Toast一样。这样就不需要申请悬浮窗权限了。
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (statusBarHeight != 0) {
            params.height = statusBarHeight * 2;
        } else {
            params.height = 60;
        }
        params.gravity = Gravity.TOP;
        windowManager = (WindowManager) getActivity().getApplication().getSystemService(getActivity().getApplication().WINDOW_SERVICE);

        floatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(floatTextView);
                if (recordService.isRunning()) {
                    recordService.stopRecord();
                    startRecord.setText(R.string.start_record);
                    stopTime();
                    //    windowManager.removeView(floatTextView);
                }

                flag = 1;
                RecordFragmentPermissionsDispatcher.toVideoCaptureActivityWithPermissionCheck(RecordFragment.this);
            }
        });
    }

    private void initLister() {
        rGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < categoryDtoList.size()) {
                    if (isInstall(categoryDtoList.get(i).getLanchUrl())) {
                        startActivity(getActivity().getPackageManager().getLaunchIntentForPackage(categoryDtoList.get(i).getLanchUrl()));
                    } else {
                        Toast.makeText(getActivity(), "未安装此应用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForCamera() {
        Toast.makeText(getActivity(), "SD卡权限拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForCamera() {
        Toast.makeText(getActivity(), "SD卡权限不再询问", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void toVideoCaptureActivity() {
        if (flag == 1) {
            VideoCaptureActivity.jumpToVideoCaptureActivity(getActivity(), Constants.RECORD_VIDEO_PATH);
        } else if (flag == 2) {
            RecordFragmentPermissionsDispatcher.startRecordWithPermissionCheck(RecordFragment.this);
        }
    }

    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void showDeniedForRecord() {
        Toast.makeText(getActivity(), "屏幕录制权限拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void showNeverAskForRecord() {
        Toast.makeText(getActivity(), "屏幕录制权限不再询问", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void startRecord() {
        Intent captureIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RecordFragmentPermissionsDispatcher.onRequestPermissionsResult(RecordFragment.this, requestCode, grantResults);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            startRecord.setEnabled(true);
            startText.setText(recordService.isRunning() ? R.string.stop_record : R.string.start_record);
            startRecord.setBackground(recordService.isRunning() ? getResources().getDrawable(R.drawable.ic_home_record_end) : getResources().getDrawable(R.drawable.ic_home_record_start));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            if (projectionManager == null) {
                return;
            }
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();
            startText.setText(R.string.stop_record);
            startRecord.setBackground(getResources().getDrawable(R.drawable.ic_home_record_end));
            //开始计时
            startTime();
        }
    }

    private void startTime() {
        timerTask = new TimerTask() {
            int cnt = 0;
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textTime.setText(getStringTime(cnt++));
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }

    private void stopTime() {
        if (!timerTask.cancel()) {
            timerTask.cancel();
            timer.cancel();
        }
    }

    private void getStatuHeight() {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInstall(String packageName) {
        PackageManager manager = getActivity().getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MeiZuPermissionUtil.checkMeizuCameraPermission(getContext());
    }
}
