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
 * Created by Wei on 2016/6/21.
 */
public class CustomerAdapter extends BaseAdapter{

    private List<Customer> customerList;
    private Context mContext;

    public CustomerAdapter(List<Customer> customerList, Context mContext) {
        this.customerList = customerList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Customer customer = (Customer) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_customer_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            viewHolder.customer_role = (TextView) convertView.findViewById(R.id.customer_role);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.customer_name.setText(customer.getCustomername());
        viewHolder.customer_role.setText(customer.getCustomertype());
        return convertView;
    }

    public void add(Customer customer) {
        if (customerList == null) {
            customerList = new ArrayList<>();
        }
        customerList.add(customer);
    }

    class ViewHolder {
        TextView customer_name;
        TextView customer_role;
    }

}
