package com.it.androidexpandablelistview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.androidexpandablelistview.adapter.ChapterAdapter;
import com.it.androidexpandablelistview.bean.Chapter;
import com.it.androidexpandablelistview.biz.ChapterBiz;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    private TextView tv_expandablelistview;
    private ImageView iv_life;
    boolean viewFlag = true;
    private Button mBtnRefresh;
    private ExpandableListView mExpandableListView;
    private BaseExpandableListAdapter mAdapter;

    private List<Chapter> mDatas=new ArrayList<>();

    private ChapterBiz mChapterBiz =new ChapterBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initEvents();
        loadDatas();
    }

    private void loadDatas() {
        mChapterBiz.loadDatas(this,new ChapterBiz.CallBack(){

            @Override
            public void onSuccess(List<Chapter> chapterList) {
                mDatas.addAll(chapterList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception ex) {
                ex.printStackTrace();
            }
        },false);
    }


    private void initUI() {
        mBtnRefresh = findViewById(R.id.id_btn_refresh);
//        tv_expandablelistview = findViewById(R.id.tv_expandablelistview);
//        tv_expandablelistview.setOnClickListener(this);
//        iv_life = findViewById(R.id.iv_life);
        mExpandableListView=findViewById(R.id.id_expandablelistview);
        mDatas.clear();
//        mDatas.addAll(ChapterLab.generateMockDatas());
        mAdapter=new ChapterAdapter(this,mDatas);
        mExpandableListView.setAdapter(mAdapter);
    }

    private void initEvents() {
        mExpandableListView.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent,
                                        View v, int groupPosition,
                                        int childPosition, long id) {
                Log.e("sange","=onChildClick groupPosition="+groupPosition+",childPosition= "+childPosition);
                return false;
            }
        });

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.e("sange","=onChildClick groupPosition="+groupPosition);
                return false;
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//            收缩
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.e("sange","=onChildClick groupPosition="+groupPosition);
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.e("sange","=onChildClick groupPosition="+groupPosition);
            }
        });

    }

}
