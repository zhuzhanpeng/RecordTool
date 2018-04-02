package com.onairm.recordtool4android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onairm.baselibrary.utils.ScreenUtils;
import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.base.BaseRVAdapter;

/**
 * Created by Edison on 2018/1/26.
 */

public class TimeSelectAdapter extends BaseRVAdapter{
    private String[] seconds={"5秒","10秒","15秒","20秒","25秒","30秒"};
    private int selectIndex=0;
    @Override
    public TimeSlectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item=View.inflate(parent.getContext(), R.layout.item_time_select,null);
        RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(
                ScreenUtils.dipConvertPx(parent.getContext(),45)
        ,ScreenUtils.dipConvertPx(parent.getContext(),45));
        params.leftMargin=ScreenUtils.dipConvertPx(parent.getContext(),7);
        params.rightMargin=ScreenUtils.dipConvertPx(parent.getContext(),7);
        item.setLayoutParams(params);
        return new TimeSlectViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TimeSlectViewHolder viewHolder= (TimeSlectViewHolder) holder;
        viewHolder.tvTimeSlect.setText(seconds[position]);
        if(selectIndex==position){
            viewHolder.tvTimeSlect.setBackgroundResource(R.drawable.bg_blue_time_select_shape);
        }else {
            viewHolder.tvTimeSlect.setBackgroundResource(R.drawable.bg_time_select_shape);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyOnItemClick(v,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return seconds.length;
    }

    static class TimeSlectViewHolder extends RecyclerView.ViewHolder{
        TextView tvTimeSlect;
        public TimeSlectViewHolder(View itemView) {
            super(itemView);
            tvTimeSlect=itemView.findViewById(R.id.tvTimeSlect);
        }
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }
}
