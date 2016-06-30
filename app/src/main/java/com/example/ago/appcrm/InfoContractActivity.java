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

public class InfoContractActivity extends AppCompatActivity {

    private EditText title_field;
    private EditText amount_field;
    private EditText client_field;
    private EditText our_field;
    private EditText remarks_field;

    private TextView confirm_btn;

    public static Contract currentContract;
    public static String currentOpportunityid;
    public static String currentCustomerid;
    private String currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_contract);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    private void bindViews() {
        title_field = (EditText) findViewById(R.id.title_field);
        amount_field = (EditText) findViewById(R.id.amount_field);
        client_field = (EditText) findViewById(R.id.client_field);
        our_field = (EditText) findViewById(R.id.our_field);
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
        if (currentContract == null) {
            currentMode = "add";
            confirm_btn.setText("确认添加");
            currentContract = new Contract();
        } else {
            currentMode = "modify";
            confirm_btn.setText("确认修改");

            title_field.setText(currentContract.getContracttitle());
            amount_field.setText(currentContract.getTotalamount());
            client_field.setText(currentContract.getClientcontractor());
            our_field.setText(currentContract.getOurcontractor());
            remarks_field.setText(currentContract.getContractremarks());
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
        currentContract.setContracttitle(title_field.getText().toString());
        currentContract.setTotalamount(amount_field.getText().toString());
        currentContract.setClientcontractor(client_field.getText().toString());
        currentContract.setOurcontractor(our_field.getText().toString());
        currentContract.setContractremarks(remarks_field.getText().toString());
    }

    private void add() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/contract_create_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("customerid="+ URLEncoder.encode(currentCustomerid, "utf-8")+
                            "&opportunityid="+URLEncoder.encode(currentOpportunityid, "utf-8")+
                            "&contracttitle="+URLEncoder.encode(currentContract.getContracttitle(), "utf-8")+
                            "&totalamount="+URLEncoder.encode(currentContract.getTotalamount(), "utf-8")+
                            "&clientcontractor="+URLEncoder.encode(currentContract.getClientcontractor(), "utf-8")+
                            "&ourcontractor="+URLEncoder.encode(currentContract.getOurcontractor(), "utf-8")+
                            "&contractremarks="+URLEncoder.encode(currentContract.getContractremarks(), "utf-8")+
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
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/contract_modify_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("contractid="+URLEncoder.encode(currentContract.getContractid(), "utf-8")+
                            "&customerid="+ URLEncoder.encode(currentContract.getCustomerid(), "utf-8")+
                            "&opportunityid="+URLEncoder.encode(currentContract.getOpportunityid(), "utf-8")+
                            "&contracttitle="+URLEncoder.encode(currentContract.getContracttitle(), "utf-8")+
                            "&totalamount="+URLEncoder.encode(currentContract.getTotalamount(), "utf-8")+
                            "&clientcontractor="+URLEncoder.encode(currentContract.getClientcontractor(), "utf-8")+
                            "&ourcontractor="+URLEncoder.encode(currentContract.getOurcontractor(), "utf-8")+
                            "&contractremarks="+URLEncoder.encode(currentContract.getContractremarks(), "utf-8")+
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
