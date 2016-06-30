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

public class ProductListActivity extends AppCompatActivity {
    
    private ListView productList;
    private TextView add_btn;
    
    private Context mContext;
    private ProductAdapter pAdapter;

    private static int all_product_currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
        setData();
    }

    private void bindViews() {
        productList = (ListView) findViewById(R.id.product_list);
        add_btn = (TextView) findViewById(R.id.add_product);
        mContext = ProductListActivity.this;

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoProductActivity.currentProduct = null;
                Intent intent = new Intent(mContext, InfoProductActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void setData() {
        all_product_currentpage = 0;
        pAdapter = new ProductAdapter(mContext, new ArrayList<Product>());
        productList.setAdapter(pAdapter);
        getAllProduct();

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoProductActivity.currentProduct = (Product) productList.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, InfoProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllProduct() {
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
                    dos.writeBytes("currentpage="+Integer.toString(++all_product_currentpage));
                    dos.flush();
                    dos.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    if ((line = reader.readLine()) != null){
                        System.out.println(line);
                        parse(line);

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

    private void parse(String str) {
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
                    message.what = 0x001;
                    message.obj = product;
                    handler.sendMessage(message);
                }

                if (object.getInt("currentpage") < object.getInt("pagecount")) {
                    getAllProduct();
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
                    //所有客户刷新
                    pAdapter.add((Product) msg.obj);
                    pAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    
}
