package com.onairm.recordtool4android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onairm.baselibrary.wedget.RoundedImageView;
import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.bean.CategoryDto;

import java.util.List;

/**
 * Created by apple on 18/1/22.
 */

public class CategoryAdapter extends BaseAdapter {
    private List<CategoryDto> categoryDtoList;
    private Context context;

    public CategoryAdapter(List<CategoryDto> categoryDtoList, Context context) {
        this.categoryDtoList = categoryDtoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryDtoList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return categoryDtoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        MyHolder myHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.category_adapter_item, viewGroup, false);
            myHolder = new MyHolder();
            myHolder.cRoundeImage = view.findViewById(R.id.cRoundeImage);
            myHolder.cTextView = view.findViewById(R.id.cTextView);
            view.setTag(myHolder);
        }
        myHolder = (MyHolder) view.getTag();
        if (position < categoryDtoList.size()) {
            myHolder.cRoundeImage.setImageResource(categoryDtoList.get(position).getAppIc());
            myHolder.cTextView.setText(categoryDtoList.get(position).getcName());
        } else {
            myHolder.cRoundeImage.setImageResource(R.mipmap.add);
            myHolder.cTextView.setText("添加应用");
        }
        return view;
    }

    public class MyHolder {
        RoundedImageView cRoundeImage;
        TextView cTextView;
    }

}
