package com.example.ago.appcrm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OpportunityDetailActivity extends AppCompatActivity {

    public static Opportunity currentOpportunity;

    private ImageView opportunity_avatar;
    private TextView opportunity_title;
    private TextView followup_tab;
    private TextView product_tab;
    private TextView contract_tab;

    private Context mContext;
    private ListView detail_list;
    private FollowupAdapter followupAdapter;
    private ContractAdapter contractAdapter;
    private ProductAdapter productAdapter;

    private static int followup_currentpage;
    private static int contract_currentpage;
    private static int product_currentpage;

    private TextView add_btn;
    private String currentListType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_detail);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();

        followup_tab.performClick();
        opportunity_title.setText(currentOpportunity.getOpportunitytitle());
    }

    private void bindViews() {
        opportunity_avatar = (ImageView) findViewById(R.id.opportunity_avatar);
        opportunity_title = (TextView) findViewById(R.id.opportunity_title);
        add_btn = (TextView) findViewById(R.id.add_btn);

        followup_tab = (TextView) findViewById(R.id.followup_tab);
        product_tab = (TextView) findViewById(R.id.product_tab);
        contract_tab = (TextView) findViewById(R.id.contract_tab);

        detail_list = (ListView) findViewById(R.id.detail_list);
        mContext = OpportunityDetailActivity.this;

        followup_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllTabUnselected();
                followup_tab.setSelected(true);
                currentListType = "followup";
                setData("followup");
            }
        });
        product_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllTabUnselected();
                product_tab.setSelected(true);
                currentListType = "product";
                setData("product");
            }
        });
        contract_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllTabUnselected();
                contract_tab.setSelected(true);
                currentListType = "contract";
                setData("contract");
            }
        });

        opportunity_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoOpportunityActivity.currentOpportunity = currentOpportunity;
                Intent intent = new Intent(mContext, InfoOpportunityActivity.class);
                startActivity(intent);
            }
        });

        opportunity_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoOpportunityActivity.currentOpportunity = currentOpportunity;
                Intent intent = new Intent(mContext, InfoOpportunityActivity.class);
                startActivity(intent);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentListType) {
                    case "product":
                        //新建产品
                        InfoProductActivity.currentProduct = null;
                        Intent intent = new Intent(mContext, InfoProductActivity.class);
                        startActivity(intent);
                        break;
                    case "followup":
                        //新建跟进
                        InfoFollowupActivity.currentFollowup = null;
                        InfoFollowupActivity.currentSourceid = currentOpportunity.getOpportunityid();
                        InfoFollowupActivity.currentSourcetype = "2";
                        Intent intent1 = new Intent(mContext, InfoFollowupActivity.class);
                        startActivity(intent1);
                        break;
                    case "contract":
                        //新建合同
                        InfoContractActivity.currentContract = null;
                        InfoContractActivity.currentOpportunityid = currentOpportunity.getOpportunityid();
                        InfoContractActivity.currentCustomerid = currentOpportunity.getCustomerid();
                        Intent intent3 = new Intent(mContext, InfoContractActivity.class);
                        startActivity(intent3);
                        break;
                }
            }
        });
    }

    private void setData(String opt) {
        switch (opt) {
            case "followup":
                followup_currentpage = 0;
                followupAdapter = new FollowupAdapter(new ArrayList<Followup>(), mContext);
                detail_list.setAdapter(followupAdapter);
                getFollowup();
                break;
            case "product":
                product_currentpage = 0;
                productAdapter = new ProductAdapter(mContext, new ArrayList<Product>());
                detail_list.setAdapter(productAdapter);
                getProduct();
                break;
            case "contract":
                contract_currentpage = 0;
                contractAdapter = new ContractAdapter(new ArrayList<Contract>(), mContext);
                detail_list.setAdapter(contractAdapter);
                getContract();
                break;
            default:
                break;
        }
    }

    private void getFollowup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_followup_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++followup_currentpage)+
                            "&sourcetype=2"+
                            "&sourceid="+currentOpportunity.getOpportunityid());
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parseFollowup(line);

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

    private void getProduct() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_product_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++product_currentpage));
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parseProduct(line);

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

    private void getContract() {
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
                    dos.writeBytes("currentpage="+Integer.toString(++contract_currentpage)+"&opportunityid="+currentOpportunity.getOpportunityid());
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parseContract(line);

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

    private void parseFollowup(String str) {
        try {
            JSONObject object = new JSONObject(str);
            int resultcode = object.getInt("resultcode");
            if (resultcode == 0) {
                //成功
                int currentcount = object.getInt("currentcount");
                JSONObject followupObject;
                for (int i = 0; i < currentcount; i++) {
                    followupObject = object.getJSONObject(Integer.toString(i));
                    Followup followup = ParseFactory.parseFollowup(followupObject);

                    Message message = new Message();
                    message.what = 0x001;
                    message.obj = followup;
                    handler.sendMessage(message);
                }

                if (object.getInt("currentpage") < object.getInt("pagecount")) {
                    getFollowup();
                }

            } else {
                //失败
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseProduct(String str) {
        try {
            JSONObject object = new JSONObject(str);
            int resultcode = object.getInt("resultcode");
            if (resultcode == 0) {
                //成功
                int currentcount = object.getInt("currentcount");
                JSONObject productObject;
                for (int i = 0; i < currentcount; i++) {
                    productObject = object.getJSONObject(Integer.toString(i));
                    Product product = ParseFactory.parseProduct(productObject);

                    Message message = new Message();
                    message.what = 0x002;
                    message.obj = product;
                    handler.sendMessage(message);
                }

                if (object.getInt("currentpage") < object.getInt("pagecount")) {
                    getProduct();
                }

            } else {
                //失败
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseContract(String str) {
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
                    message.what = 0x003;
                    message.obj = contract;
                    handler.sendMessage(message);
                }

                if (object.getInt("currentpage") < object.getInt("pagecount")) {
                    getContract();
                }

            } else {
                //失败
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    //跟进刷新
                    followupAdapter.add((Followup) msg.obj);
                    followupAdapter.notifyDataSetChanged();
                    break;
                case 0x003:
                    //合同刷新
                    contractAdapter.add((Contract) msg.obj);
                    contractAdapter.notifyDataSetChanged();
                    break;
                case 0x002:
                    //产品刷新
                    productAdapter.add((Product) msg.obj);
                    productAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void setAllTabUnselected() {
        followup_tab.setSelected(false);
        product_tab.setSelected(false);
        contract_tab.setSelected(false);
    }
}
