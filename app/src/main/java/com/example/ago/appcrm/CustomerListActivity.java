package com.example.ago.appcrm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity {

    private ListView customerList;
    private TextView all_customer_tab;
    private TextView my_customer_tab;

    private Context mContext;
    private List<Customer> customers;
    private CustomerAdapter allAdapter;
    private CustomerAdapter myAdapter;

    private static int all_customer_currentpage;
    private static int my_customer_currentpage;

    private TextView add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
        all_customer_tab.performClick();
    }

    private void bindViews() {
        customerList = (ListView) findViewById(R.id.customer_list);
        mContext = CustomerListActivity.this;
        all_customer_tab = (TextView) findViewById(R.id.all_customer_tab);
        my_customer_tab = (TextView) findViewById(R.id.my_customer_tab);

        add_btn = (TextView) findViewById(R.id.add_customer);

        all_customer_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CustomerListActivity", "所有客户 点击");
                my_customer_tab.setSelected(false);
                all_customer_tab.setSelected(true);

                setData("all");
            }
        });
        my_customer_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CustomerListActivity", "我的客户 点击");
                all_customer_tab.setSelected(false);
                my_customer_tab.setSelected(true);

                setData("my");
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CustomerListActivity", "添加客户 点击");
                InfoCustomerActivity.currentCustomer = null;
                Intent intent = new Intent(CustomerListActivity.this, InfoCustomerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setData(String opt) {
        switch (opt) {
            case "all" :
                all_customer_currentpage = 0;
                customers = new ArrayList<>();
                allAdapter = new CustomerAdapter(customers, mContext);
                customerList.setAdapter(allAdapter);
                getAllCustomer();
                break;
            case "my" :
                my_customer_currentpage = 0;
                customers = new ArrayList<>();
                myAdapter = new CustomerAdapter(customers, mContext);
                customers = new ArrayList<>();
                customerList.setAdapter(myAdapter);
                getMyCustomer();
                break;
        }

        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerDetailActivity.currentCustomer = (Customer) customerList.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, CustomerDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    //所有客户刷新
                    allAdapter.add((Customer) msg.obj);
                    allAdapter.notifyDataSetChanged();
                    break;
                case 0x002:
                    //我的客户刷新
                    myAdapter.add((Customer) msg.obj);
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void getAllCustomer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_customer_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++all_customer_currentpage));
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parse(line, "all");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    private void getMyCustomer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_customer_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++my_customer_currentpage)+"&staffid="+MainActivity.staffid);
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parse(line, "my");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    private void parse(String str, String opt) {
        try {
            JSONObject object = new JSONObject(str);
            int resultcode = object.getInt("resultcode");
            if (resultcode == 0) {
                //成功
                int currentcount = object.getInt("currentcount");
                JSONObject customerObject;
                for (int i = 0; i < currentcount; i++) {
                    customerObject = object.getJSONObject(Integer.toString(i));
                    Customer customer = ParseFactory.parseCustomer(customerObject);

                    Message message = new Message();
                    message.obj = customer;

                    switch (opt) {
                        case "all" :
                            message.what = 0x001;
                            handler.sendMessage(message);
                            break;
                        case "my" :
                            message.what = 0x002;
                            handler.sendMessage(message);
                            break;
                    }
                }

                if (object.getInt("currentpage") < object.getInt("pagecount")) {
                    switch (opt) {
                        case "all" :
                            getAllCustomer();
                            break;
                        case "my" :
                            getMyCustomer();
                            break;
                    }
                }

            } else {
                //失败
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
