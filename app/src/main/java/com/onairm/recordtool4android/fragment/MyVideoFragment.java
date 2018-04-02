package com.onairm.recordtool4android.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.adapter.MyVideoAdapter;
import com.onairm.recordtool4android.bean.MyLiveList;
import com.onairm.recordtool4android.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class MyVideoFragment extends Fragment implements MyVideoAdapter.OnItemClickListener, View.OnClickListener, MyVideoAdapter.OnItemShareClickLister {

    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;
    RecyclerView mRecyclerview;
    TextView mTvSelectNum;
    Button mBtnDelete;
    TextView mSelectAll;
    LinearLayout mLlMycollectionBottomDialog;
    TextView mBtnEditor;
    private MyVideoAdapter mRadioAdapter = null;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MyLiveList> mList = new ArrayList<>();
    private int mEditMode = MYLIVE_MODE_CHECK;
    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;
    private ImageView select_all_image;

    private Dialog shareDialog;
    private TextView dialog_share;
    private TextView dialog_delete;
    private TextView dialog_cancle;

    public static MyVideoFragment newInstance() {
        MyVideoFragment fragment = new MyVideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_video, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        initListener();
        initDialog();
    }

    private void initDialog() {
        shareDialog = new Dialog(getActivity(), R.style.DialogShareBottom);
        Window window = shareDialog.getWindow();
        window.setContentView(R.layout.dialog_share_layout);
        initDialog(window);
        shareDialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = shareDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth());
        shareDialog.getWindow().setAttributes(lp);
    }

    private void initDialog(Window window) {
        dialog_share = (TextView) window.findViewById(R.id.dialog_share);
        dialog_delete = (TextView) window.findViewById(R.id.dialog_delete);
        dialog_cancle = (TextView) window.findViewById(R.id.dialog_cancle);

        dialog_share.setOnClickListener(this);
        dialog_delete.setOnClickListener(this);
        dialog_cancle.setOnClickListener(this);

    }


    private void initView(View view) {
        mRecyclerview = view.findViewById(R.id.recyclerview);
        mTvSelectNum = view.findViewById(R.id.tv_select_num);
        mBtnDelete = view.findViewById(R.id.btn_delete);
        mSelectAll = view.findViewById(R.id.select_all);
        select_all_image = (ImageView) view.findViewById(R.id.select_all_image);
        mLlMycollectionBottomDialog = view.findViewById(R.id.ll_mycollection_bottom_dialog);
        mBtnEditor = view.findViewById(R.id.btn_editor);
    }

    private void initData() {
        mRadioAdapter = new MyVideoAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_main_bg_height_1));
        mRecyclerview.addItemDecoration(itemDecorationHeader);
        mRecyclerview.setAdapter(mRadioAdapter);

        for (int i = 0; i < 30; i++) {
            MyLiveList myLiveList = new MyLiveList();
            myLiveList.setTitle("这是第" + i + "个条目");
            myLiveList.setSource("来源" + i);
            mList.add(myLiveList);
            mRadioAdapter.notifyAdapter(mList, false);
        }
    }

    private void setBtnBackground(int size) {
//        if (size != 0) {
//            mBtnDelete.setBackgroundResource(R.drawable.button_shape);
//            mBtnDelete.setEnabled(true);
//            mBtnDelete.setTextColor(Color.WHITE);
//        } else {
//            mBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
//            mBtnDelete.setEnabled(false);
//            mBtnDelete.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_b7b8bd));
//        }
    }


    private void initListener() {
        mRadioAdapter.setOnItemClickListener(this);
        mRadioAdapter.setOnItemShareClickLister(this);
        mBtnDelete.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        select_all_image.setOnClickListener(this);
        mBtnEditor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:

                break;


            case R.id.dialog_share:

                break;

            case R.id.dialog_delete:

                break;

            case R.id.dialog_cancle:
                shareDialog.dismiss();
                break;
            case R.id.select_all:
                selectAllMain();
                break;
            case R.id.btn_editor:
                updataEditMode();
                break;

            case R.id.select_all_image:
                selectAllMain();
                break;
            default:
                break;
        }
    }

    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (mRadioAdapter == null) return;
        if (!isSelectAll) {
            for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                mRadioAdapter.getMyLiveList().get(i).setSelect(true);
            }
            index = mRadioAdapter.getMyLiveList().size();
            mBtnDelete.setEnabled(true);
            mSelectAll.setText("取消全选");
            select_all_image.setImageResource(R.mipmap.ic_oval_hover);
            isSelectAll = true;
        } else {
            for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                mRadioAdapter.getMyLiveList().get(i).setSelect(false);
            }
            index = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            select_all_image.setImageResource(R.mipmap.ic_oval);
            isSelectAll = false;
        }
        mRadioAdapter.notifyDataSetChanged();
        setBtnBackground(index);
        mTvSelectNum.setText(String.valueOf(index));
    }


//    private void deleteVideo() {
//        if (index == 0){
//            mBtnDelete.setEnabled(false);
//            return;
//        }
//        final AlertDialog builder = new AlertDialog.Builder(this)
//                .create();
//        builder.show();
//        if (builder.getWindow() == null) return;
//        builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
//        TextView msg = (TextView) builder.findViewById(R.id.tv_msg);
//        Button cancle = (Button) builder.findViewById(R.id.btn_cancle);
//        Button sure = (Button) builder.findViewById(R.id.btn_sure);
//        if (msg == null || cancle == null || sure == null) return;
//
//        if (index == 1) {
//            msg.setText("删除后不可恢复，是否删除该条目？");
//        } else {
//            msg.setText("删除后不可恢复，是否删除这" + index + "个条目？");
//        }
//        cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.dismiss();
//            }
//        });
//        sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i = mRadioAdapter.getMyLiveList().size(), j =0 ; i > j; i--) {
//                    MyLiveList myLive = mRadioAdapter.getMyLiveList().get(i-1);
//                    if (myLive.isSelect()) {
//
//                        mRadioAdapter.getMyLiveList().remove(myLive);
//                        index--;
//                    }
//                }
//                index = 0;
//                mTvSelectNum.setText(String.valueOf(0));
//                setBtnBackground(index);
//                if (mRadioAdapter.getMyLiveList().size() == 0){
//                    mLlMycollectionBottomDialog.setVisibility(View.GONE);
//                }
//                mRadioAdapter.notifyDataSetChanged();
//                builder.dismiss();
//            }
//        });
//    }
//

    private void updataEditMode() {
        mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT : MYLIVE_MODE_CHECK;
        if (mEditMode == MYLIVE_MODE_EDIT) {
            mBtnEditor.setText("取消");
            mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            mBtnEditor.setText("编辑");

            mLlMycollectionBottomDialog.setVisibility(View.GONE);
            editorStatus = false;
            clearAll();
        }
        mRadioAdapter.setEditMode(mEditMode);
    }


    private void clearAll() {
        mTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        mSelectAll.setText("全选");
        select_all_image.setImageResource(R.mipmap.ic_oval);
        for (int i = 0; i < mRadioAdapter.getMyLiveList().size(); i++) {
            mRadioAdapter.getMyLiveList().get(i).setSelect(false);
        }
        mRadioAdapter.notifyDataSetChanged();
        setBtnBackground(0);
    }

    @Override
    public void onItemClickListener(int pos, List<MyLiveList> myLiveList) {
        if (editorStatus) {
            MyLiveList myLive = myLiveList.get(pos);
            boolean isSelect = myLive.isSelect();
            if (!isSelect) {
                index++;
                myLive.setSelect(true);
                if (index == myLiveList.size()) {
                    isSelectAll = true;
                    mSelectAll.setText("取消全选");
                    select_all_image.setImageResource(R.mipmap.ic_oval_hover);
                }
            } else {
                myLive.setSelect(false);
                index--;
                isSelectAll = false;
                mSelectAll.setText("全选");
                select_all_image.setImageResource(R.mipmap.ic_oval);
            }
            setBtnBackground(index);
            mTvSelectNum.setText(String.valueOf(index));
            mRadioAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemShareClickLister(int pos, List<MyLiveList> myLiveList) {
        shareDialog.show();

    }
}




