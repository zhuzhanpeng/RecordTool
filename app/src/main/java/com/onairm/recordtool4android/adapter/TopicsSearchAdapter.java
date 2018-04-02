package com.onairm.recordtool4android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onairm.recordtool4android.R;

import java.util.List;

/**
 * Created by bqy on 2018/1/26.
 */

public class TopicsSearchAdapter extends BaseAdapter {

    private List<String> stringList;
    private Context context;

    public TopicsSearchAdapter(List<String> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_topic_search, null);
            holder.searchItem = convertView.findViewById(R.id.tvItemTopic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.searchItem.setText(stringList.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView searchItem;
        RelativeLayout rlItem;
    }
}
