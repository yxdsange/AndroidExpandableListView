package com.it.androidexpandablelistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.androidexpandablelistview.R;
import com.it.androidexpandablelistview.bean.Chapter;
import com.it.androidexpandablelistview.bean.ChapterItem;

import java.util.List;

public class ChapterAdapter extends BaseExpandableListAdapter {
    private List<Chapter> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;

    public ChapterAdapter(Context context, List<Chapter> chapter) {
        mContext = context;
        mDatas = chapter;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
//        判断是否有稳定的ID 对缓存影响，
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder;
        if (convertView == null) {
            // TODO: 2020/3/7
            convertView = mInflater.inflate(R.layout.item_parent_chapter, parent, false);
            parentViewHolder = new ParentViewHolder();
            parentViewHolder.tv = convertView.findViewById(R.id.id_tv_parent);
            parentViewHolder.iv = convertView.findViewById(R.id.id_indicator_group);
            convertView.setTag(parentViewHolder);

//            初始化ViewHolder
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        Chapter chapter=mDatas.get(groupPosition);
        parentViewHolder.tv.setText(mDatas.get(groupPosition).getName());
        parentViewHolder.iv.setSelected(isExpanded);
        parentViewHolder.iv.setImageResource(R.drawable.group_indicator);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_child_chapter, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv = convertView.findViewById(R.id.id_tv_child);
            convertView.setTag(childViewHolder);

//            初始化ViewHolder
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();

        }
//        ChapterItem chapterItem=mDatas.get(groupPosition).getChildren().get(childPosition);
        childViewHolder.tv.setText(mDatas.get(groupPosition).getChildren().get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ParentViewHolder {
        TextView tv;
        ImageView iv;
    }

    public static class ChildViewHolder {
        TextView tv;

    }

}
