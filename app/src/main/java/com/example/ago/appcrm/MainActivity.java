package com.example.ago.appcrm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String staffid = "115";
    public static String customerid = "515";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout company_customer_layout = (RelativeLayout) findViewById(R.id.company_customer);
        RelativeLayout company_contract_layout = (RelativeLayout) findViewById(R.id.company_contract);
        RelativeLayout company_opportunity_layout = (RelativeLayout) findViewById(R.id.company_opportunity);
        RelativeLayout company_business_layout = (RelativeLayout) findViewById(R.id.company_business);
        RelativeLayout company_contacts_layout = (RelativeLayout) findViewById(R.id.company_contacts);
        RelativeLayout company_product_layout = (RelativeLayout) findViewById(R.id.company_product);

        //更改tab_name
        TextView t = (TextView) company_customer_layout.findViewById(R.id.tab_name);
        t.setText("公司客户");
        t = (TextView) company_opportunity_layout.findViewById(R.id.tab_name);
        t.setText("公司商机");
        t = (TextView) company_contract_layout.findViewById(R.id.tab_name);
        t.setText("已创建合同");
        t = (TextView) company_business_layout.findViewById(R.id.tab_name);
        t.setText("业务管理");
        t = (TextView) company_contacts_layout.findViewById(R.id.tab_name);
        t.setText("联系人");
        t = (TextView) company_product_layout.findViewById(R.id.tab_name);
        t.setText("公司产品");

        //更改tab_number

        //设置按钮监听事件
        company_business_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "业务管理 点击");
            }
        });
        company_contacts_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "联系人 点击");
                Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });
        company_contract_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "已创建合同 点击");
                Intent intent = new Intent(MainActivity.this, ContractListActivity.class);
                startActivity(intent);
            }
        });
        company_product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "公司产品 点击");
                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });
        company_opportunity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "公司商机 点击");
                Intent intent = new Intent(MainActivity.this, OpportunityListActivity.class);
                startActivity(intent);
            }
        });
        company_customer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "公司客户 点击");
                Intent intent = new Intent(MainActivity.this, CustomerListActivity.class);
                startActivity(intent);
            }
        });
    }
}
