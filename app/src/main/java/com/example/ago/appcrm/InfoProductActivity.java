package com.example.ago.appcrm;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class InfoProductActivity extends AppCompatActivity {

    public static Product currentProduct;
    private String currentMode;

    private EditText name_field;
    private EditText sn_field;
    private EditText standard_field;
    private EditText salesunit_field;
    private EditText introduction_field;
    private EditText remarks_field;

    private TextView confirm_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_product);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    private void bindViews() {
        name_field = (EditText) findViewById(R.id.name_field);
        sn_field = (EditText) findViewById(R.id.sn_field);
        standard_field = (EditText) findViewById(R.id.standard_field);
        salesunit_field = (EditText) findViewById(R.id.salesunit_field);
        introduction_field = (EditText) findViewById(R.id.introduction_field);
        remarks_field = (EditText) findViewById(R.id.remarks_field);

        confirm_btn = (TextView) findViewById(R.id.confirm_btn);

        render();

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void render() {
        if (currentProduct == null) {
            currentMode = "add";
            confirm_btn.setText("确认添加");
            currentProduct = new Product();
        } else {
            currentMode = "modify";
            confirm_btn.setText("确认修改");

            name_field.setText(currentProduct.getProductname());
            sn_field.setText(currentProduct.getProductsn());
            standard_field.setText(currentProduct.getStandardprice());
            salesunit_field.setText(currentProduct.getSalesunit());
            introduction_field.setText(currentProduct.getIntroduction());
            remarks_field.setText(currentProduct.getProductremarks());
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
        currentProduct.setProductname(name_field.getText().toString());
        currentProduct.setProductsn(sn_field.getText().toString());
        currentProduct.setStandardprice(standard_field.getText().toString());
        currentProduct.setSalesunit(salesunit_field.getText().toString());
        currentProduct.setIntroduction(introduction_field.getText().toString());
        currentProduct.setProductremarks(remarks_field.getText().toString());
    }

    private void add() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/product_create_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("productname="+ URLEncoder.encode(currentProduct.getProductname(), "utf-8")+
                            "&productsn="+URLEncoder.encode(currentProduct.getProductsn(), "utf-8")+
                            "&standardprice="+URLEncoder.encode(currentProduct.getStandardprice(), "utf-8")+
                            "&salesunit="+URLEncoder.encode(currentProduct.getSalesunit(), "utf-8")+
                            "&introduction="+URLEncoder.encode(currentProduct.getIntroduction(), "utf-8")+
                            "&productremarks="+currentProduct.getProductremarks());

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

    private void modify() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/product_modify_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("productid="+URLEncoder.encode(currentProduct.getProductid(), "utf-8")+
                            "&productname="+ URLEncoder.encode(currentProduct.getProductname(), "utf-8")+
                            "&productsn="+URLEncoder.encode(currentProduct.getProductsn(), "utf-8")+
                            "&standardprice="+URLEncoder.encode(currentProduct.getStandardprice(), "utf-8")+
                            "&salesunit="+URLEncoder.encode(currentProduct.getSalesunit(), "utf-8")+
                            "&introduction="+URLEncoder.encode(currentProduct.getIntroduction(), "utf-8")+
                            "&productremarks="+currentProduct.getProductremarks());

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
