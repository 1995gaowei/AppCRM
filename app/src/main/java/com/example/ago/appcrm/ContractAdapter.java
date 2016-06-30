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
public class ContractAdapter extends BaseAdapter {

    private List<Contract> contractList;
    private Context mContext;

    public ContractAdapter(List<Contract> contractList, Context mContext) {
        this.contractList = contractList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return contractList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return contractList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contract contract = (Contract) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contract_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contract_customer = (TextView) view.findViewById(R.id.contract_customer);
            viewHolder.contract_name = (TextView) view.findViewById(R.id.contract_name);
            viewHolder.contract_price = (TextView) view.findViewById(R.id.contract_price);
            viewHolder.contract_role = (TextView) view.findViewById(R.id.contract_role);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.contract_name.setText(contract.getContracttitle());
        viewHolder.contract_role.setText(contract.getContracttype());
        viewHolder.contract_customer.setText(contract.getCustomername());
        viewHolder.contract_price.setText(contract.getEstimatedamount()+"元");
        return view;
    }

    public void add(Contract contract) {
        if (contractList == null) {
            contractList = new ArrayList<>();
        }
        contractList.add(contract);
    }

    class ViewHolder {
        TextView contract_name;
        TextView contract_role;
        TextView contract_price;
        TextView contract_customer;
    }
}
