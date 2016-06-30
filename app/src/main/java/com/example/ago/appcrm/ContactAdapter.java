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
 * Created by Wei on 2016/6/22.
 */
public class ContactAdapter extends BaseAdapter {

    private List<Contact> contactList;
    private Context mContext;

    public ContactAdapter(List<Contact> contactList, Context context) {
        this.contactList = contactList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = (Contact) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contact_name = (TextView) view.findViewById(R.id.contact_name);
            viewHolder.contact_from = (TextView) view.findViewById(R.id.contact_from);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.contact_name.setText(contact.getContactsname());
        viewHolder.contact_from.setText("来自客户 "+contact.getCustomername());
        return view;
    }

    public void add(Contact contact) {
        if (contactList == null) {
            contactList = new ArrayList<>();
        }
        contactList.add(contact);
    }

    class ViewHolder {
        TextView contact_name;
        TextView contact_from;
    }
}
