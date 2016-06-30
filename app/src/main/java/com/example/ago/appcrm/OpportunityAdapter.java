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
 * Created by Wei on 2016/6/23.
 */
public class OpportunityAdapter extends BaseAdapter {

    private List<Opportunity> opportunityList;
    private Context mContext;

    public OpportunityAdapter(List<Opportunity> opportunityList, Context mContext) {
        this.mContext = mContext;
        this.opportunityList = opportunityList;
    }

    @Override
    public int getCount() {
        return opportunityList.size();
    }

    @Override
    public Object getItem(int position) {
        return opportunityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Opportunity opportunity = (Opportunity) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_opportunity_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.opportunity_customer = (TextView) view.findViewById(R.id.opportunity_customer);
            viewHolder.opportunity_name = (TextView) view.findViewById(R.id.opportunity_name);
            viewHolder.opportunity_price = (TextView) view.findViewById(R.id.opportunity_price);
            viewHolder.opportunity_role = (TextView) view.findViewById(R.id.opportunity_role);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.opportunity_name.setText(opportunity.getOpportunitytitle());
        viewHolder.opportunity_role.setText(opportunity.getBusinesstype());
        viewHolder.opportunity_customer.setText(opportunity.getCustomername());
        viewHolder.opportunity_price.setText(opportunity.getEstimatedamount()+"元");
        return view;
    }

    public void add(Opportunity opportunity) {
        if (opportunityList == null) {
            opportunityList = new ArrayList<>();
        }
        opportunityList.add(opportunity);
    }

    class ViewHolder {
        TextView opportunity_name;
        TextView opportunity_role;
        TextView opportunity_price;
        TextView opportunity_customer;
    }
}
