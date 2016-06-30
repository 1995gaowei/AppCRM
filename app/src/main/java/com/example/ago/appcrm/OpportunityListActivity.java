package com.example.ago.appcrm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class OpportunityListActivity extends AppCompatActivity {

    private ListView opportunityList;
    private TextView all_opportunity_tab;
    private TextView my_opportunity_tab;

    private Context mContext;
    private List<Opportunity> opportunities;
    private OpportunityAdapter allAdapter;
    private OpportunityAdapter myAdapter;

    private static int all_opportunity_currentpage;
    private static int my_opportunity_currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
        all_opportunity_tab.performClick();
    }

    private void bindViews(){
        opportunityList = (ListView) findViewById(R.id.opportunity_list);
        mContext = OpportunityListActivity.this;
        all_opportunity_tab = (TextView) findViewById(R.id.all_opportunity_tab);
        my_opportunity_tab = (TextView) findViewById(R.id.my_opportunity_tab);

        all_opportunity_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_opportunity_tab.setSelected(false);
                all_opportunity_tab.setSelected(true);

                setData("all");
            }
        });

        my_opportunity_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_opportunity_tab.setSelected(false);
                my_opportunity_tab.setSelected(true);

                setData("my");
            }
        });
    }

    private void setData(String opt) {
        switch (opt) {
            case "all" :
                all_opportunity_currentpage = 0;
                opportunities = new ArrayList<>();
                allAdapter = new OpportunityAdapter(opportunities, mContext);
                opportunityList.setAdapter(allAdapter);
                getAllOpportunity();
                break;
            case "my" :
                my_opportunity_currentpage = 0;
                opportunities = new ArrayList<>();
                myAdapter = new OpportunityAdapter(opportunities, mContext);
                opportunityList.setAdapter(myAdapter);
                getMyOpportunity();
                break;
        }
        opportunityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpportunityDetailActivity.currentOpportunity = (Opportunity) opportunityList.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, OpportunityDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    //所有商机刷新
                    allAdapter.add((Opportunity) msg.obj);
                    allAdapter.notifyDataSetChanged();
                    break;
                case 0x002:
                    //我的商机刷新
                    myAdapter.add((Opportunity) msg.obj);
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void getAllOpportunity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_opportunity_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++all_opportunity_currentpage));
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

    private void getMyOpportunity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_opportunity_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++my_opportunity_currentpage)+"&staffid="+MainActivity.staffid);
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
                JSONObject opportunityObject;
                for (int i = 0; i < currentcount; i++) {
                    opportunityObject = object.getJSONObject(Integer.toString(i));
                    Opportunity opportunity = ParseFactory.parseOpportunity(opportunityObject);

                    Message message = new Message();
                    message.obj = opportunity;

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
                            getAllOpportunity();
                            break;
                        case "my" :
                            getMyOpportunity();
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
