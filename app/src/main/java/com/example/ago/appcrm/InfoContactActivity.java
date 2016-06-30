package com.example.ago.appcrm;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class InfoContactActivity extends AppCompatActivity {

    private EditText name_field;
    private EditText mobile_field;
    private EditText age_field;
    private RadioGroup gender_field;
    private EditText telephone_field;
    private EditText email_field;
    private EditText address_field;
    private EditText zipcode_field;
    private EditText remarks_field;
    private EditText qq_field;
    private EditText wechat_field;

    private TextView confirm_btn;

    public static Contact currentContact;
    public static String currentCustomerid;
    private String currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_contact);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    private void bindViews() {
        name_field = (EditText) findViewById(R.id.name_field);
        mobile_field = (EditText) findViewById(R.id.mobile_field);
        age_field = (EditText) findViewById(R.id.age_field);
        gender_field = (RadioGroup) findViewById(R.id.gender_field);
        telephone_field = (EditText) findViewById(R.id.telephone_field);
        email_field = (EditText) findViewById(R.id.email_field);
        address_field = (EditText) findViewById(R.id.address_field);
        zipcode_field = (EditText) findViewById(R.id.zipcode_field);
        remarks_field = (EditText) findViewById(R.id.remarks_field);
        qq_field = (EditText) findViewById(R.id.qq_field);
        wechat_field = (EditText) findViewById(R.id.wechat_field);

        confirm_btn = (TextView) findViewById(R.id.confirm_btn);

        render();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击确认");
                confirm();
            }
        });

        gender_field.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                currentContact.setContactsgender(rb.getText().toString());
            }
        });
    }

    private void render() {
        if (currentContact == null) {
            currentMode = "add";
            confirm_btn.setText("确认添加");
            currentContact = new Contact();
        } else {
            confirm_btn.setText("确认修改");
            currentMode = "modify";

            name_field.setText(currentContact.getContactsname());
            email_field.setText(currentContact.getContactsemail());
            address_field.setText(currentContact.getContactsaddress());
            zipcode_field.setText(currentContact.getContactszipcode());
            telephone_field.setText(currentContact.getContactstelephone());
            remarks_field.setText(currentContact.getContactsremarks());
            mobile_field.setText(currentContact.getContactsmobile());
            wechat_field.setText(currentContact.getContactswechat());
            qq_field.setText(currentContact.getContactsqq());
            age_field.setText(currentContact.getContactsage());
            switch (currentContact.getContactsgender()) {
                case "男":
                    findViewById(R.id.gender_male).performClick();
                    break;
                case "女":
                    findViewById(R.id.gender_female).performClick();
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
        currentContact.setContactsaddress(address_field.getText().toString());
        currentContact.setContactsage(age_field.getText().toString());
        currentContact.setContactsemail(email_field.getText().toString());
        currentContact.setContactsmobile(mobile_field.getText().toString());
        currentContact.setContactsname(name_field.getText().toString());
        currentContact.setContactsqq(qq_field.getText().toString());
        currentContact.setContactsremarks(remarks_field.getText().toString());
        currentContact.setContactstelephone(telephone_field.getText().toString());
        currentContact.setContactswechat(wechat_field.getText().toString());
        currentContact.setContactszipcode(zipcode_field.getText().toString());
    }

    private void modify() {
        System.out.println("修改联系人");

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/contact_modify_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("contactsid="+ URLEncoder.encode(currentContact.getContactsid(), "utf-8")+
                            "&contactsname="+URLEncoder.encode(currentContact.getContactsname(), "utf-8")+
                            "&contactstelephone="+URLEncoder.encode(currentContact.getContactstelephone(), "utf-8")+
                            "&contactsage="+URLEncoder.encode(currentContact.getContactsage(), "utf-8")+
                            "&contactsmobile="+URLEncoder.encode(currentContact.getContactsmobile(), "utf-8")+
                            "&contactsemail="+URLEncoder.encode(currentContact.getContactsemail(), "utf-8")+
                            "&contactsgender="+URLEncoder.encode(currentContact.getContactsgender(), "utf-8")+
                            "&contactsaddress="+URLEncoder.encode(currentContact.getContactsaddress(), "utf-8")+
                            "&contactszipcode="+URLEncoder.encode(currentContact.getContactszipcode(), "utf-8")+
                            "&contactsremarks="+URLEncoder.encode(currentContact.getContactsremarks(), "utf-8")+
                            "&wechat="+URLEncoder.encode(currentContact.getContactswechat(), "utf-8")+
                            "&qq="+URLEncoder.encode(currentContact.getContactsqq(), "utf-8")+
                            "&customerid="+URLEncoder.encode(currentContact.getCustomerid(), "utf-8"));

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
        System.out.println("添加联系人");

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/contact_create_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("contactsname="+URLEncoder.encode(currentContact.getContactsname(), "utf-8")+
                            "&contactstelephone="+URLEncoder.encode(currentContact.getContactstelephone(), "utf-8")+
                            "&contactsage="+URLEncoder.encode(currentContact.getContactsage(), "utf-8")+
                            "&contactsmobile="+URLEncoder.encode(currentContact.getContactsmobile(), "utf-8")+
                            "&contactsemail="+URLEncoder.encode(currentContact.getContactsemail(), "utf-8")+
                            "&contactsgender="+URLEncoder.encode(currentContact.getContactsgender(), "utf-8")+
                            "&contactsaddress="+URLEncoder.encode(currentContact.getContactsaddress(), "utf-8")+
                            "&contactszipcode="+URLEncoder.encode(currentContact.getContactszipcode(), "utf-8")+
                            "&contactsremarks="+URLEncoder.encode(currentContact.getContactsremarks(), "utf-8")+
                            "&wechat="+URLEncoder.encode(currentContact.getContactswechat(), "utf-8")+
                            "&qq="+URLEncoder.encode(currentContact.getContactsqq(), "utf-8")+
                            "&customerid="+currentCustomerid);

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
