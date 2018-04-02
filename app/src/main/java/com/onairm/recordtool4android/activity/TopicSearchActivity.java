package com.onairm.recordtool4android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.adapter.TopicsSearchAdapter;
import com.onairm.recordtool4android.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bqy on 2018/1/25.
 */

public class TopicSearchActivity extends BaseActivity {

    private EditText searchEdit;
    private TextView btnSearch;
    private ListView searchRecyclerHis;
    private ImageView searchBack;

    private List<String> searchList;
    private TopicsSearchAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_search);

        searchEdit = findViewById(R.id.searchEdit);
        btnSearch = findViewById(R.id.btnSearch);
        searchRecyclerHis = findViewById(R.id.searchRecyclerHis);
        searchBack = findViewById(R.id.searchBack);


        searchList = new ArrayList<>();
        searchList.add("神马玩意儿");
        searchList.add("神马玩意儿");
        searchList.add("神马玩意儿");
        searchList.add("神马玩意儿");
        searchList.add("神马玩意儿");
        searchList.add("神马玩意儿");
        searchList.add("神马玩意儿");
        adapter = new TopicsSearchAdapter(searchList, TopicSearchActivity.this);
        searchRecyclerHis.setAdapter(adapter);
        searchRecyclerHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("topic", searchList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchRecyclerHis.requestFocus();
    }

}
