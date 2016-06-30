package com.example.ago.appcrm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ListView contactList;
    private TextView all_contact_tab;
    private TextView my_contact_tab;

    private Context mContext;
    private List<Contact> contacts;
    private ContactAdapter allAdapter;
    private ContactAdapter myAdapter;

    private static int all_contact_currentpage;
    private static int my_contact_currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
        all_contact_tab.performClick();
    }

    private void bindViews() {
        contactList = (ListView) findViewById(R.id.contact_list);
        mContext = ContactListActivity.this;
        all_contact_tab = (TextView) findViewById(R.id.all_contact_tab);
        my_contact_tab = (TextView) findViewById(R.id.my_contact_tab);

        all_contact_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CustomerListActivity", "所有联系人 点击");
                my_contact_tab.setSelected(false);
                all_contact_tab.setSelected(true);

                setData("all");
            }
        });
        my_contact_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CustomerListActivity", "我的联系人 点击");
                all_contact_tab.setSelected(false);
                my_contact_tab.setSelected(true);

                setData("my");
            }
        });

    }

    private void setData(String opt) {
        switch (opt) {
            case "all" :
                all_contact_currentpage = 0;
                contacts = new ArrayList<>();
                allAdapter = new ContactAdapter(contacts, mContext);
                contactList.setAdapter(allAdapter);
                getAllContact();
                break;
            case "my" :
                my_contact_currentpage = 0;
                contacts = new ArrayList<>();
                myAdapter = new ContactAdapter(contacts, mContext);
                contactList.setAdapter(myAdapter);
                getMyContact();
                break;
        }
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoContactActivity.currentContact = (Contact) contactList.getAdapter().getItem(position);
                Intent intent = new Intent(ContactListActivity.this, InfoContactActivity.class);
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    //所有联系人刷新
                    allAdapter.add((Contact) msg.obj);
                    allAdapter.notifyDataSetChanged();
                    break;
                case 0x002:
                    //我的联系人刷新
                    myAdapter.add((Contact) msg.obj);
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void getAllContact() {
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
                    dos.writeBytes("currentpage="+Integer.toString(++all_contact_currentpage));
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

    private void getMyContact() {
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
                    dos.writeBytes("currentpage="+Integer.toString(++my_contact_currentpage)+"&staffid="+MainActivity.staffid);
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
                JSONObject contactObject;
                for (int i = 0; i < currentcount; i++) {
                    contactObject = object.getJSONObject(Integer.toString(i));
                    Contact contact = ParseFactory.parseContact(contactObject);

                    Message message = new Message();
                    message.obj = contact;

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
                            getAllContact();
                            break;
                        case "my" :
                            getMyContact();
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
