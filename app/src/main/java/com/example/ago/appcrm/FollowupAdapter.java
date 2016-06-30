package com.example.ago.appcrm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wei on 2016/6/28.
 */
public class FollowupAdapter extends BaseAdapter {

    List<Followup> followupList;
    Context mContext;

    public FollowupAdapter(List<Followup> followupList, Context mContext) {
        this.followupList = followupList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return followupList.size();
    }

    @Override
    public Object getItem(int position) {
        return followupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Followup followup = (Followup) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_followup_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.staff = (TextView) convertView.findViewById(R.id.staff);
            viewHolder.followup_content = (TextView) convertView.findViewById(R.id.follow_content);
            viewHolder.followup_time = (TextView) convertView.findViewById(R.id.follow_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.staff.setText(followup.getName());
        viewHolder.followup_time.setText(followup.getCreatetime());
        viewHolder.followup_content.setText(followup.getContent());
        return convertView;
    }

    public void add(Followup followup) {
        if (followupList == null) {
            followupList = new ArrayList<>();
        }
        followupList.add(followup);
    }

    class ViewHolder {
        TextView staff;
        TextView followup_time;
        TextView followup_content;
    }
}
