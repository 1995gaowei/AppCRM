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

public class ContractListActivity extends AppCompatActivity {

    private ListView contractList;
    private TextView all_contract_tab;
    private TextView my_contract_tab;

    private Context mContext;
    private List<Contract> contracts;
    private ContractAdapter allAdapter;
    private ContractAdapter myAdapter;

    private static int all_contract_currentpage;
    private static int my_contract_currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
        all_contract_tab.performClick();
    }

    private void bindViews(){
        contractList = (ListView) findViewById(R.id.contract_list);
        mContext = ContractListActivity.this;
        all_contract_tab = (TextView) findViewById(R.id.all_contract_tab);
        my_contract_tab = (TextView) findViewById(R.id.my_contract_tab);

        all_contract_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_contract_tab.setSelected(false);
                all_contract_tab.setSelected(true);

                setData("all");
            }
        });

        my_contract_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_contract_tab.setSelected(false);
                my_contract_tab.setSelected(true);

                setData("my");
            }
        });
    }

    private void setData(String opt) {
        switch (opt) {
            case "all" :
                all_contract_currentpage = 0;
                contracts = new ArrayList<>();
                allAdapter = new ContractAdapter(contracts, mContext);
                contractList.setAdapter(allAdapter);
                getAllContract();
                break;
            case "my" :
                my_contract_currentpage = 0;
                contracts = new ArrayList<>();
                myAdapter = new ContractAdapter(contracts, mContext);
                contractList.setAdapter(myAdapter);
                getMyContract();
                break;
        }
        contractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoContractActivity.currentContract = (Contract) contractList.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, InfoContractActivity.class);
                startActivity(intent);
            }
        });

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    //所有合同刷新
                    allAdapter.add((Contract) msg.obj);
                    allAdapter.notifyDataSetChanged();
                    break;
                case 0x002:
                    //我的合同刷新
                    myAdapter.add((Contract) msg.obj);
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void getAllContract() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_contract_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++all_contract_currentpage));
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

    private void getMyContract() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_contract_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++my_contract_currentpage)+"&staffid="+MainActivity.staffid);
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
                JSONObject contractObject;
                for (int i = 0; i < currentcount; i++) {
                    contractObject = object.getJSONObject(Integer.toString(i));
                    Contract contract = ParseFactory.parseContract(contractObject);

                    Message message = new Message();
                    message.obj = contract;

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
                            getAllContract();
                            break;
                        case "my" :
                            getMyContract();
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
