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

public class InfoOpportunityActivity extends AppCompatActivity {

    private EditText title_field;
    private EditText amount_field;
    private EditText successrate_field;
    private EditText remarks_field;

    private TextView confirm_btn;

    public static Opportunity currentOpportunity;
    public static String currentCustomerid;
    private String currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opportunity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    private void bindViews() {
        title_field = (EditText) findViewById(R.id.title_field);
        amount_field = (EditText) findViewById(R.id.amount_field);
        successrate_field = (EditText) findViewById(R.id.successrate_field);
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

    private void add() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/opportunity_create_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("opportunitytitle="+URLEncoder.encode(currentOpportunity.getOpportunitytitle(), "utf-8")+
                            "&estimatedamount="+URLEncoder.encode(currentOpportunity.getEstimatedamount(), "utf-8")+
                            "&successrate="+URLEncoder.encode(currentOpportunity.getSuccessrate(), "utf-8")+
                            "&opportunityremarks="+URLEncoder.encode(currentOpportunity.getOpportunityremarks(), "utf-8")+
                            "&customerid="+URLEncoder.encode(currentCustomerid, "utf-8")+
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

    private void modify() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/opportunity_modify_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("opportunityid="+ URLEncoder.encode(currentOpportunity.getOpportunityid(), "utf-8")+
                            "&opportunitytitle="+URLEncoder.encode(currentOpportunity.getOpportunitytitle(), "utf-8")+
                            "&estimatedamount="+URLEncoder.encode(currentOpportunity.getEstimatedamount(), "utf-8")+
                            "&successrate="+URLEncoder.encode(currentOpportunity.getSuccessrate(), "utf-8")+
                            "&opportunityremarks="+URLEncoder.encode(currentOpportunity.getOpportunityremarks(), "utf-8")+
                            "&customerid="+URLEncoder.encode(currentOpportunity.getCustomerid(), "utf-8")+
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

    private void render() {
        if (currentOpportunity == null) {
            currentMode = "add";
            confirm_btn.setText("确认添加");
            currentOpportunity = new Opportunity();
        } else {
            confirm_btn.setText("确认修改");
            currentMode = "modify";

            title_field.setText(currentOpportunity.getOpportunitytitle());
            amount_field.setText(currentOpportunity.getEstimatedamount());
            successrate_field.setText(currentOpportunity.getSuccessrate());
            remarks_field.setText(currentOpportunity.getOpportunityremarks());
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
        currentOpportunity.setOpportunitytitle(title_field.getText().toString());
        currentOpportunity.setEstimatedamount(amount_field.getText().toString());
        currentOpportunity.setSuccessrate(successrate_field.getText().toString());
        currentOpportunity.setOpportunityremarks(remarks_field.getText().toString());
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
