package com.example.ago.appcrm;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class InfoCustomerActivity extends AppCompatActivity {

    private EditText name_field;
    private EditText profile_field;
    private RadioGroup type_field;
    private EditText telephone_field;
    private EditText email_field;
    private EditText website_field;
    private EditText address_field;
    private EditText zipcode_field;
    private EditText remarks_field;

    private TextView confirm_btn;

    public static Customer currentCustomer;
    public String currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_customer);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    private void bindViews() {
        name_field = (EditText) findViewById(R.id.name_field);
        profile_field = (EditText) findViewById(R.id.profile_field);
        type_field = (RadioGroup) findViewById(R.id.type_field);
        telephone_field = (EditText) findViewById(R.id.telephone_field);
        email_field = (EditText) findViewById(R.id.email_field);
        website_field = (EditText) findViewById(R.id.website_field);
        address_field = (EditText) findViewById(R.id.address_field);
        zipcode_field = (EditText) findViewById(R.id.zipcode_field);
        remarks_field = (EditText) findViewById(R.id.remarks_field);

        confirm_btn = (TextView) findViewById(R.id.confirm_btn);

        render();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        type_field.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                currentCustomer.setCustomertype(rb.getText().toString());
            }
        });
    }

    private void render() {
        if (currentCustomer == null) {
            currentMode = "add";
            confirm_btn.setText("确认添加");
            currentCustomer = new Customer();
        } else {
            currentMode = "modify";
            confirm_btn.setText("确认修改");

            name_field.setText(currentCustomer.getCustomername());
            profile_field.setText(currentCustomer.getProfile());
            email_field.setText(currentCustomer.getEmail());
            address_field.setText(currentCustomer.getAddress());
            zipcode_field.setText(currentCustomer.getZipcode());
            telephone_field.setText(currentCustomer.getTelephone());
            website_field.setText(currentCustomer.getWebsite());
            remarks_field.setText(currentCustomer.getCustomerremarks());
            switch (currentCustomer.getCustomertype()) {
                case "重要客户":
                    findViewById(R.id.type1).performClick();
                    break;
                case "一般客户":
                    findViewById(R.id.type2).performClick();
                    break;
                case "低价值客户":
                    findViewById(R.id.type3).performClick();
                    break;
                default:
                    break;
            }
        }
    }

    private void confirm() {
        switch (currentMode){
            case "add":
                setInfo();
                add();
                break;
            case "modify":
                setInfo();
                modify();
                break;
            default:
                break;
        }
    }

    private void setInfo() {
        currentCustomer.setCustomername(name_field.getText().toString());
        currentCustomer.setProfile(profile_field.getText().toString());
        currentCustomer.setTelephone(telephone_field.getText().toString());
        currentCustomer.setEmail(email_field.getText().toString());
        currentCustomer.setAddress(address_field.getText().toString());
        currentCustomer.setWebsite(website_field.getText().toString());
        currentCustomer.setZipcode(zipcode_field.getText().toString());
        currentCustomer.setCustomerremarks(remarks_field.getText().toString());
    }

    private void modify() {
        Log.d("CustomerListActivity", currentCustomer.getCustomername());

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/customer_modify_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("customerid="+URLEncoder.encode(currentCustomer.getCustomerid(), "utf-8")+
                            "&customername="+URLEncoder.encode(currentCustomer.getCustomername(), "utf-8")+
                            "&telephone="+URLEncoder.encode(currentCustomer.getTelephone(), "utf-8")+
                            "&profile="+URLEncoder.encode(currentCustomer.getProfile(), "utf-8")+
                            "&customertype="+URLEncoder.encode(ParseFactory.customerTypeReverse(currentCustomer.getCustomertype()), "utf-8")+
                            "&email="+URLEncoder.encode(currentCustomer.getEmail(), "utf-8")+
                            "&website="+URLEncoder.encode(currentCustomer.getWebsite(), "utf-8")+
                            "&address="+URLEncoder.encode(currentCustomer.getAddress(), "utf-8")+
                            "&zipcode="+URLEncoder.encode(currentCustomer.getZipcode(), "utf-8")+
                            "&customerremarks="+URLEncoder.encode(currentCustomer.getCustomerremarks(), "utf-8")+
                            "&staffid="+MainActivity.staffid);

                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        handler.sendEmptyMessage(0x002);
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

    private void add() {
        Log.d("CustomerListActivity", currentCustomer.getCustomername());

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/customer_create_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("customername="+URLEncoder.encode(currentCustomer.getCustomername(), "utf-8")+
                            "&telephone="+URLEncoder.encode(currentCustomer.getTelephone(), "utf-8")+
                            "&profile="+URLEncoder.encode(currentCustomer.getProfile(), "utf-8")+
                            "&customertype="+URLEncoder.encode(ParseFactory.customerTypeReverse(currentCustomer.getCustomertype()), "utf-8")+
                            "&email="+URLEncoder.encode(currentCustomer.getEmail(), "utf-8")+
                            "&website="+URLEncoder.encode(currentCustomer.getWebsite(), "utf-8")+
                            "&address="+URLEncoder.encode(currentCustomer.getAddress(), "utf-8")+
                            "&zipcode="+URLEncoder.encode(currentCustomer.getZipcode(), "utf-8")+
                            "&customerremarks="+URLEncoder.encode(currentCustomer.getCustomerremarks(), "utf-8")+
                            "&staffid="+MainActivity.staffid);
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        handler.sendEmptyMessage(0x001);
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    //添加完成
                    finish();
                    break;
                case 0x002:
                    //修改完成
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

}
