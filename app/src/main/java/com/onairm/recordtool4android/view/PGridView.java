package com.onairm.recordtool4android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class PGridView extends GridView {
	public PGridView(Context context) {

		super(context);
	}
	public PGridView(Context context, AttributeSet attrs) {

		super(context, attrs);

	}
	public PGridView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}


	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(
				Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
