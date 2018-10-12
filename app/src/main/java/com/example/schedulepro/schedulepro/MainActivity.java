package com.example.schedulepro.schedulepro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private int index = 0;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mTextMessage.setText("加载数据ing...");
            thead.start();
            //
            return true;
        }
    };
    //    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            mTextMessage.setText(msg.obj + "");
//        }
//    };
    private Thread thead = new Thread(new Runnable() {
        @Override
        public void run() {
            // 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
            String url = "http://www.superps.cn:1337/com/hello?孟光";

            // 生成请求对象
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();

            // 发送请求
            try {

                HttpResponse response = httpClient.execute(httpGet);
                final String result = EntityUtils.toString(response.getEntity(), "utf-8");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextMessage.setText(result + "--" + (index++));
                    }
                });
                //Message msg = new Message();
                //msg.obj = result + "--" + (index++);
                //thead.sleep(1000);
                //handler.handleMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        //MainActivity.this.runOnUiThread(thead);
        thead.start();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        int id=navigation.getMenu().
//        MenuItem item= (MenuItem)findViewById(id);
//        mTextMessage.setText(id+"");
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
