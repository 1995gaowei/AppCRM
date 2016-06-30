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
public class ProductAdapter extends BaseAdapter {

    private List<Product> productList;
    private Context mContext;

    public ProductAdapter(Context context, List<Product> productList) {
        this.mContext = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = (Product) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_product_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.product_name = (TextView) view.findViewById(R.id.product_name);
            viewHolder.product_number = (TextView) view.findViewById(R.id.product_number);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.product_name.setText(product.getProductname());
        viewHolder.product_number.setText(product.getProductsn());
        return view;
    }

    public void add(Product product) {
        if (productList == null) {
            productList = new ArrayList<Product>();
        }
        productList.add(product);
    }

    class ViewHolder {
        TextView product_name;
        TextView product_number;
    }

}
