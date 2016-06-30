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

public class InfoFollowupActivity extends AppCompatActivity {

    public static Followup currentFollowup;
    public static String currentSourceid;
    public static String currentSourcetype;
    private String currentMode;

    private EditText content_field;
    private EditText remarks_field;
    private TextView confirm_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_followup);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    private void bindViews() {
        content_field = (EditText) findViewById(R.id.content_field);
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
        if (currentFollowup == null) {
            currentMode = "add";
            confirm_btn.setText("确认添加");
            currentFollowup = new Followup();
        } else {
            currentMode = "modify";
            confirm_btn.setText("确认修改");

            content_field.setText(currentFollowup.getContent());
            remarks_field.setText(currentFollowup.getFollowupremarks());
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
        currentFollowup.setContent(content_field.getText().toString());
        currentFollowup.setFollowupremarks(remarks_field.getText().toString());
    }

    private void add() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/followup_create_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("sourcetype="+ URLEncoder.encode(currentSourcetype, "utf-8")+
                            "&sourceid="+URLEncoder.encode(currentSourceid, "utf-8")+
                            "&content="+URLEncoder.encode(currentFollowup.getContent(), "utf-8")+
                            "&followupremarks="+URLEncoder.encode(currentFollowup.getFollowupremarks(), "utf-8")+
                            "&creatorid="+MainActivity.staffid);

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
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/followup_modify_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("followupid="+URLEncoder.encode(currentFollowup.getFollowupid(), "utf-8")+
                            "&sourcetype="+ URLEncoder.encode(currentSourcetype, "utf-8")+
                            "&sourceid="+URLEncoder.encode(currentSourceid, "utf-8")+
                            "&content="+URLEncoder.encode(currentFollowup.getContent(), "utf-8")+
                            "&followupremarks="+URLEncoder.encode(currentFollowup.getFollowupremarks(), "utf-8")+
                            "&creatorid="+MainActivity.staffid);

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
