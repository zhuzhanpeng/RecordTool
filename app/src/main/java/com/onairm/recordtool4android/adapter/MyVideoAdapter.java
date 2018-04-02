package com.onairm.recordtool4android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.bean.MyLiveList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guohao on 2017/9/6.
 */

public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {

    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;

    private int secret = 0;
    private String title = "";
    private Context context;
    private List<MyLiveList> mMyLiveList;
    private OnItemClickListener mOnItemClickListener;

    public MyVideoAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(List<MyLiveList> myLiveList, boolean isAdd) {
        if (!isAdd) {
            this.mMyLiveList = myLiveList;
        } else {
            this.mMyLiveList.addAll(myLiveList);
        }
        notifyDataSetChanged();
    }

    public List<MyLiveList> getMyLiveList() {
        if (mMyLiveList == null) {
            mMyLiveList = new ArrayList<>();
        }
        return mMyLiveList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_live, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mMyLiveList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MyLiveList myLive = mMyLiveList.get(holder.getAdapterPosition());
        holder.mTvTitle.setText(myLive.getTitle());
        if (mEditMode == MYLIVE_MODE_CHECK) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
            if (myLive.isSelect()) {
                holder.mCheckBox.setImageResource(R.mipmap.ic_oval_hover);
            } else {
                holder.mCheckBox.setImageResource(R.mipmap.ic_oval);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), mMyLiveList);
            }
        });


        holder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemShareClickLister!=null)
                    onItemShareClickLister.onItemShareClickLister(holder.getAdapterPosition(),mMyLiveList);
            }
        });


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos, List<MyLiveList> myLiveList);
    }


    public interface OnItemShareClickLister{
        void onItemShareClickLister(int pos, List<MyLiveList> myLiveList);
    }
    private OnItemShareClickLister onItemShareClickLister;
    public void setOnItemShareClickLister(OnItemShareClickLister onItemShareClickLister) {
        this.onItemShareClickLister = onItemShareClickLister;
    }

    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mRadioImg;
        TextView mTvTitle;
        ImageView mCheckBox;
        ImageView tvShare;

        public ViewHolder(View itemView) {
            super(itemView);
            mRadioImg = itemView.findViewById(R.id.radio_img);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mCheckBox = itemView.findViewById(R.id.check_box);
            tvShare = itemView.findViewById(R.id.tvShare);
        }
    }


}
