package com.example.schedulepro.schedulepro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private int index = 0;
    private String userName = "";
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            mTextMessage.setText(String.format("加载%s数据ing...",item.getTitle()));
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
            if(userName==""){
                return;
            }
            // 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
            String url = "http://www.superps.cn:1337/com/hello?" + userName;

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
                        mTextMessage.setText(result);
                    }
                });
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
        thead.start();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Button btnCommit = (Button) findViewById(R.id.commit);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"asd",Toast.LENGTH_LONG).show();
                TextView txtUsr = (TextView) findViewById(R.id.txt);
                MainActivity.this.userName = txtUsr.getText().toString();
                thead.start();
            }
        });
    }

}
