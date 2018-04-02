package com.onairm.recordtool4android.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onairm.baselibrary.utils.ScreenUtils;
import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.base.BaseRVAdapter;
import com.onairm.recordtool4android.utils.Constants;

/**
 * Created by Edison on 2018/1/26.
 */

public class PosterAdapter extends BaseRVAdapter{
    private Bitmap[] bitmaps;
    private int selectIndex=0;

    public PosterAdapter(Bitmap[] bitmaps){
        this.bitmaps=bitmaps;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_poster_adapter,null);
        RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
        , ScreenUtils.dipConvertPx(parent.getContext(),67));
        view.setLayoutParams(params);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PosterViewHolder viewHolder = (PosterViewHolder) holder;
        viewHolder.ivPoster.setImageBitmap(bitmaps[position]);
        if(selectIndex==position){
            viewHolder.ivPoster.setBackgroundResource(R.drawable.bg_poster_frame_shape);
        }else{
            viewHolder.ivPoster.setBackground(null);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyOnItemClick(v,position);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notifyOnItemLongClick(view, position);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return Constants.THUMB_COUNT;
    }

    final static class PosterViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPoster;
        public PosterViewHolder(View itemView) {
            super(itemView);
            ivPoster=itemView.findViewById(R.id.ivPoster);
        }
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }
}
