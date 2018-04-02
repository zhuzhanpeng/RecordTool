package com.onairm.recordtool4android.base;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Edison on 2017/12/6.
 */

public  abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private OnItemKeyListener onItemKeyListener;
    private OnItemClickListener mOnItemClick;
    private OnItemLongClickListener longClickListener;
    private OnItemSelectListener mSelectListener;

    protected void notifyItemSelected(final View v,final int positon){
        if(null != mSelectListener){
            mSelectListener.onItemSelect(v,positon);
        }
    }
    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.mSelectListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.mOnItemClick = clickListener;
    }
    public void setOnLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
    public interface OnItemSelectListener {
        void onItemSelect(View view, int position);
    }
    protected void notifyOnItemLongClick(final View view,final int position){
        if(null != longClickListener){
            longClickListener.onItemLongClick(view,position);
        }
    }
    protected void notifyOnItemClick(final View view,final int position){
        if(null != mOnItemClick){
            mOnItemClick.onItemClick(view,position);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(final View view, final int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(final View view, final int position);
    }
    public void setOnItemKeyListener(OnItemKeyListener onItemKeyListener) {
        this.onItemKeyListener = onItemKeyListener;
    }


    protected boolean notifyOnItemKey(final View view,int keyCode,KeyEvent keyEvent,int position){
        if(null != onItemKeyListener){
            return onItemKeyListener.onKey(view,keyCode,keyEvent,position);
        }else {
            return false;
        }
    }

    public interface OnItemKeyListener {
        boolean onKey(View view, int keyCode, KeyEvent keyEvent, int position);
    }

}
