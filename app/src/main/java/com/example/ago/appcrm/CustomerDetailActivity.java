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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity {

    public static Customer currentCustomer;

    private ImageView customer_avatar;
    private TextView customer_name;
    private TextView followup_tab;
    private TextView contact_tab;
    private TextView opportunity_tab;
    private TextView contract_tab;

    private Context mContext;
    private ListView detail_list;
    private FollowupAdapter followupAdapter;
    private ContactAdapter contactAdapter;
    private ContractAdapter contractAdapter;
    private OpportunityAdapter opportunityAdapter;

    private static int followup_currentpage;
    private static int contact_currentpage;
    private static int contract_currentpage;
    private static int opportunity_currentpage;

    private TextView add_btn;
    private String currentListType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindView();
        followup_tab.performClick();

        customer_name.setText(currentCustomer.getCustomername());
    }

    private void bindView() {
        customer_avatar = (ImageView) findViewById(R.id.customer_avatar);
        customer_name = (TextView) findViewById(R.id.customer_name);
        add_btn = (TextView) findViewById(R.id.add_btn);

        followup_tab = (TextView) findViewById(R.id.followup_tab);
        contact_tab = (TextView) findViewById(R.id.contact_tab);
        opportunity_tab = (TextView) findViewById(R.id.opportunity_tab);
        contract_tab = (TextView) findViewById(R.id.contract_tab);

        detail_list = (ListView) findViewById(R.id.detail_list);
        mContext = CustomerDetailActivity.this;

        followup_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllTabUnselected();
                followup_tab.setSelected(true);
                currentListType = "followup";
                setData("followup");
            }
        });
        contact_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllTabUnselected();
                contact_tab.setSelected(true);
                currentListType = "contact";
                setData("contact");
            }
        });
        opportunity_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllTabUnselected();
                opportunity_tab.setSelected(true);
                currentListType = "opportunity";
                setData("opportunity");
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

        customer_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoCustomerActivity.currentCustomer = currentCustomer;
                Intent intent = new Intent(CustomerDetailActivity.this, InfoCustomerActivity.class);
                startActivity(intent);
            }
        });

        customer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoCustomerActivity.currentCustomer = currentCustomer;
                Intent intent = new Intent(CustomerDetailActivity.this, InfoCustomerActivity.class);
                startActivity(intent);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentListType) {
                    case "followup":
                        //新建跟进
                        InfoFollowupActivity.currentFollowup = null;
                        InfoFollowupActivity.currentSourceid = currentCustomer.getCustomerid();
                        InfoFollowupActivity.currentSourcetype = "1";
                        Intent intent1 = new Intent(mContext, InfoFollowupActivity.class);
                        startActivity(intent1);
                        break;
                    case "contact":
                        //新建联系人
                        InfoContactActivity.currentContact = null;
                        InfoContactActivity.currentCustomerid = currentCustomer.getCustomerid();
                        Intent intent2 = new Intent(mContext, InfoContactActivity.class);
                        startActivity(intent2);
                        break;
                    case "opportunity":
                        //新建商机
                        InfoOpportunityActivity.currentOpportunity = null;
                        InfoOpportunityActivity.currentCustomerid = currentCustomer.getCustomerid();
                        Intent intent3 = new Intent(mContext, InfoOpportunityActivity.class);
                        startActivity(intent3);
                        break;
                    case "contract":
                        break;
                }
            }
        });
    }

    private void setData(String opt) {
        switch (opt) {
            case "followup":
                add_btn.setVisibility(View.VISIBLE);
                followup_currentpage = 0;
                followupAdapter = new FollowupAdapter(new ArrayList<Followup>(), mContext);
                detail_list.setAdapter(followupAdapter);
                getFollowup();
                break;
            case "contact":
                add_btn.setVisibility(View.VISIBLE);
                contact_currentpage = 0;
                contactAdapter = new ContactAdapter(new ArrayList<Contact>(), mContext);
                detail_list.setAdapter(contactAdapter);
                getContact();
                break;
            case "contract":
                add_btn.setVisibility(View.INVISIBLE);
                contract_currentpage = 0;
                contractAdapter = new ContractAdapter(new ArrayList<Contract>(), mContext);
                detail_list.setAdapter(contractAdapter);
                getContract();
                break;
            case "opportunity":
                add_btn.setVisibility(View.VISIBLE);
                opportunity_currentpage = 0;
                opportunityAdapter = new OpportunityAdapter(new ArrayList<Opportunity>(), mContext);
                detail_list.setAdapter(opportunityAdapter);
                getOpportunity();
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
                            "&sourcetype=1"+
                            "&sourceid="+currentCustomer.getCustomerid());
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

    private void getContact() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/common_contacts_json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);   //需要输出
                    conn.setDoInput(true);   //需要输入
                    conn.setUseCaches(false);  //不允许缓存
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes("currentpage="+Integer.toString(++contact_currentpage)+"&customerid="+currentCustomer.getCustomerid());
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parseContact(line);

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
                    dos.writeBytes("currentpage="+Integer.toString(++contract_currentpage)+"&customerid="+currentCustomer.getCustomerid());
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

    private void getOpportunity() {
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
                    dos.writeBytes("currentpage="+Integer.toString(++opportunity_currentpage)+"&customerid="+currentCustomer.getCustomerid());
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parseOpportunity(line);

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
                    message.what = 0x004;
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

    private void parseContact(String str) {
        try {
            JSONObject object = new JSONObject(str);
            int resultcode = object.getInt("resultcode");
            if (resultcode == 0) {
                //成功
                int currentcount = object.getInt("currentcount");
                JSONObject contactObject;
                for (int i = 0; i < currentcount; i++) {
                    contactObject = object.getJSONObject(Integer.toString(i));
                    Contact contact = ParseFactory.parseContact(contactObject);

                    Message message = new Message();
                    message.what = 0x001;
                    message.obj = contact;
                    handler.sendMessage(message);
                }

                if (object.getInt("currentpage") < object.getInt("pagecount")) {
                    getContact();
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
                    message.what = 0x002;
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

    private void parseOpportunity(String str) {
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
                    message.what = 0x003;
                    message.obj = opportunity;
                    handler.sendMessage(message);
                }

                if (object.getInt("currentpage") < object.getInt("pagecount")) {
                    getOpportunity();
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
                    //联系人刷新
                    contactAdapter.add((Contact) msg.obj);
                    contactAdapter.notifyDataSetChanged();
                    break;
                case 0x002:
                    //合同刷新
                    contractAdapter.add((Contract) msg.obj);
                    contractAdapter.notifyDataSetChanged();
                    break;
                case 0x003:
                    //商机刷新
                    opportunityAdapter.add((Opportunity) msg.obj);
                    opportunityAdapter.notifyDataSetChanged();
                    break;
                case 0x004:
                    //跟进刷新
                    followupAdapter.add((Followup) msg.obj);
                    followupAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void setAllTabUnselected() {
        followup_tab.setSelected(false);
        contact_tab.setSelected(false);
        contract_tab.setSelected(false);
        opportunity_tab.setSelected(false);
    }


}
