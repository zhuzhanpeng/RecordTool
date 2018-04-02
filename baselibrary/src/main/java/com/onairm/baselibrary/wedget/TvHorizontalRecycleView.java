package com.onairm.baselibrary.wedget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by txt on 2015/12/3.
 *
 *
 * adapter中要增加的内容
 @Override
 public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.itemView.setFocusable(true);
    holder.itemView.setTag(position);
    holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                currentPosition = (int) holder.itemView.getTag();
                mSelectListener.onItemSelect(holder.itemView, currentPosition);
            }
        }
    });
    if(mListener!=null){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v,holder.getLayoutPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onItemLongClick(v,holder.getLayoutPosition());
                return true;
            }
        });
    }
 }

 private OnItemSelectListener mSelectListener;

 public void setOnItemSelectListener(OnItemSelectListener listener){
    mSelectListener = listener;
 }

 public interface OnItemSelectListener{
    void onItemSelect(View view,int position);
 }

 private OnItemClickListener mListener;

 public void setOnItemClickListener(OnItemClickListener listener){
    mListener = listener;
 }

 public interface OnItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view,int position);
 }

 activity中调用

 mAdapter.setOnItemSelectListener(new GalleryAdapter.OnItemSelectListener() {
     @Override
     public void onItemSelect(View view, int position) {
        mRecyclerView.smoothToCenter(position);
     }
 });
 */
public class TvHorizontalRecycleView extends RecyclerView {
    private static final String TAG = TvHorizontalRecycleView.class.getSimpleName();
    private Scroller mScroller;
    private int mLastx = 0;
    private int mTargetPos;
    //用于设置自动平移时候的速度
    private float mPxPerMillsec = 0;




    public TvHorizontalRecycleView(Context context) {
        super(context);
        init(context);
    }

    public TvHorizontalRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TvHorizontalRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        mScroller = new Scroller(context);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        //computeScrollOffset返回true表示滚动还在继续，持续时间应该就是startScroll设置的时间
        if(mScroller!=null && mScroller.computeScrollOffset()){
            Log.d(TAG, "getCurrX = " + mScroller.getCurrX());
            scrollBy(mLastx - mScroller.getCurrX(), 0);
            mLastx = mScroller.getCurrX();
            postInvalidate();//让系统继续重绘，则会继续重复执行computeScroll
        }
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy,int duration) {
        int dx=0;
        int dy=0;
        if(fx!=0) {
            dx = fx - mScroller.getFinalX();
        }
        if(fy!=0) {
            dy = fy - mScroller.getFinalY();
        }
        Log.i(TAG, "fx:" + fx + "  getfinalx:" + mScroller.getFinalX() + "  dx:" + dx);
        smoothScrollBy(dx, dy,duration);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy,int duration) {
        if(duration>0){
            mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy,duration);
        }else {
            //设置mScroller的滚动偏移量
            mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        }
        /**
         * 重绘整个view，重绘过程会调用到computeScroll()方法
         */
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    public void checkAutoAdjust(int position){
        int childcount = getChildCount();
        //获取可视范围内的选项的头尾位置
        int firstvisiableposition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int lastvisiableposition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        Log.d(TAG, "childcount:" + childcount + "  position:" + position + "  firstvisiableposition:" + firstvisiableposition
                + "  lastvisiableposition:" + lastvisiableposition);
        if(position == (firstvisiableposition + 1) || position == firstvisiableposition){
            //当前位置需要向右平移
            leftScrollBy(position, firstvisiableposition);
        }
        else if(position == (lastvisiableposition - 1) || position == lastvisiableposition){
            //当前位置需要向做平移
            rightScrollBy(position, lastvisiableposition);
        }
    }

    private void leftScrollBy(int position, int firstvisiableposition){
        View leftChild = getChildAt(0);
        if(leftChild != null){
            int leftx = leftChild.getLeft();
            Log.d(TAG, "leftChild left:" + leftx);
            int startleft = leftx;
            int endleft = position == firstvisiableposition?leftChild.getWidth():0;
            Log.d(TAG, "startleft:" + startleft + " endleft" + endleft);
            autoAdjustScroll(startleft, endleft);
        }
    }


    private void rightScrollBy(int position, int lastvisiableposition){
        int childcount = getChildCount();
        View rightChild = getChildAt(childcount - 1);
        if(rightChild != null){
            int rightx = rightChild.getRight();
            int dx = rightx - getWidth();
            Log.d(TAG, "rightChild right:" + rightx + " dx:" + dx);
            int startright = dx;
            int endright = position == lastvisiableposition?-1 * rightChild.getWidth():0;
            Log.d(TAG,"startright:" + startright + " endright:" + endright);
            autoAdjustScroll(startright, endright);
        }
    }

    /**
     *
     * @param start 滑动起始位置
     * @param end 滑动结束位置
     */
    private void autoAdjustScroll(int start, int end){
        int duration = 0;
        if(mPxPerMillsec != 0){
            duration = (int)((Math.abs(end - start)/mPxPerMillsec));
        }
        Log.d(TAG, "duration:" + duration);
        mLastx = start;
        if(duration>0) {
            mScroller.startScroll(start, 0, end - start, 0, duration);
        }else{
            mScroller.startScroll(start, 0, end - start, 0);
        }
        postInvalidate();
    }

    /**
     * 将指定item平滑移动到整个view的中间位置
     * @param position
     */
    public void smoothToCenter(int position){
        int parentWidth = getWidth();//获取父视图的宽度
        int childCount = getChildCount();//获取当前视图可见子view的总数
        //获取可视范围内的选项的头尾位置
        int firstvisiableposition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int lastvisiableposition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        int count = ((LinearLayoutManager)getLayoutManager()).getItemCount();//获取item总数
        Log.i(TAG,"count:"+count);
        mTargetPos = Math.max(0, Math.min(count - 1, position));//获取目标item的位置（参考listview中的smoothScrollToPosition方法）
        Log.i(TAG, "firstposition:" + firstvisiableposition + "   lastposition:" + lastvisiableposition + "   position:" + position+
                "   mTargetPos:"+mTargetPos);
        View targetChild = getChildAt(mTargetPos-firstvisiableposition);//获取目标item在当前可见视图item集合中的位置
        View firstChild = getChildAt(0);//当前可见视图集合中的最左view
        View lastChild = getChildAt(childCount-1);//当前可见视图集合中的最右view
        Log.i(TAG,"first-->left:"+firstChild.getLeft()+"   right:"+firstChild.getRight());
        Log.i(TAG, "last-->left:" + lastChild.getLeft() + "   right:" + lastChild.getRight());

        int childLeftPx =0;
        int childRightPx =0;
        int childWidth =0;
        int centerLeft=0;
        int centerRight =0;
        if (targetChild!=null){
            childLeftPx = targetChild.getLeft();//子view相对于父view的左边距
            childRightPx = targetChild.getRight();//子view相对于父view的右边距
            childWidth = targetChild.getWidth();
            centerLeft = parentWidth/2-childWidth/2;//计算子view居中后相对于父view的左边距
            centerRight = parentWidth/2+childWidth/2;//计算子view居中后相对于父view的右边距
        }

        Log.i(TAG, "target-->left:" + targetChild.getLeft() + "   right:" + targetChild.getRight());
        Log.i(TAG,"rv width:"+parentWidth+"   item width:"+childWidth+"   centerleft:"+centerLeft+"   centerRight:"+centerRight);
        if(childLeftPx>centerLeft){//子view左边距比居中view大（说明子view靠父view的右边，此时需要把子view向左平移
            //平移的起始位置就是子view的左边距，平移的距离就是两者之差
            mLastx = childLeftPx;
            mScroller.startScroll(childLeftPx,0,centerLeft-childLeftPx,0,600);//600为移动时长，可自行设定
            postInvalidate();
        }else if(childRightPx<centerRight){
            mLastx = childRightPx;
            mScroller.startScroll(childRightPx,0,centerRight-childRightPx,0,600);
            postInvalidate();
        }


    }




    // 添加滑动到顶部
    private  int mIndex=0;//要置顶项的位置
    private int firstVItem=0;//调用moveToPostion滑动后获取屏幕可见的第一项
    private boolean move = false;
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        //在这里进行第二次滚动（最后的距离）
        if (move) {
            move = false;
            //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
            int n =  mIndex- firstVItem;
            if (0 <= n && n <this.getChildCount()) {
                //获取要置顶的项顶部离RecyclerView顶部的距离
                int top = this.getChildAt(n).getTop();
                //最后的移动
                this.scrollBy(0, top);
            }
        }
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    /**
     * 处理要置顶问题
     *
     * @param moveToPosition 要移动到的item位置
     * @return  返回置顶后item的所在位置
     */

    public int moveToPosition(int moveToPosition) {
        //获取当前recycleView屏幕可见的第一项和最后一项的Position
        int firstItem=getFirstVisiblePosition();
        int lastItem=getLastVisiblePosition();

        //  Log.i("TAG","moveToPosition firstItem :"+firstItem +" || lastItem :"+lastItem +"|| index :"+index);

        mIndex=moveToPosition;
        //然后区分情况
        if (moveToPosition <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            this.scrollToPosition(moveToPosition);
        } else if (moveToPosition <= lastItem) {
            //当要置顶的项已经在屏幕上显示时，计算它离屏幕原点的距离
            int top = this.getChildAt(moveToPosition - firstItem).getTop();
            this.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            this.scrollToPosition(moveToPosition);
            //记录当前需要在RecyclerView滚动监听里面继续第二次滚动
            move = true;
        }
        return moveToPosition;
    }

    public int getFirstVisiblePosition() {
        if (getChildCount() == 0)
            return 0;
        else
            return getChildLayoutPosition(getChildAt(0));
    }

    public int getLastVisiblePosition() {
        final int childCount = getChildCount();
        if (childCount == 0)
            return 0;
        else
            return getChildLayoutPosition(getChildAt(childCount - 1));
    }


}
