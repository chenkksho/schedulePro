package com.example.schedulepro.schedulepro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import java.net.URLEncoder;
import java.util.function.Function;

public class MainActivity extends Activity {


    private TextView mTextMessage;
    private int index = 0;
    private String userName = "";
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            mTextMessage.setText(String.format("加载%s数据ing...", item.getTitle()));
//            thead.start();
            //
            return true;
        }
    };
    //    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            mTextMessage.setText(msg.obj + "");
//        }
//    };

    private Runnable run=new Runnable() {
        @Override
        public void run() {
            if (userName == "") {
                return;
            }
            // 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
            String url = "http://www.superps.cn:1337/com/hi?usr=" + URLEncoder.encode(userName);
//            String url = "http://www.superps.cn:1337/com/hi";

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
    };


    //设置系统状态栏为透明,并且会取消占用的空间
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //设置状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //判断当前设备版本号是否为4.4以上，如果是，则通过调用setTranslucentStatus让状态栏变透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true); //隐藏状态栏
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

//        //获取顶部View，动态设置高度
//        View paddingView = findViewById(R.id.paddingView);
//        ViewGroup.LayoutParams params = paddingView.getLayoutParams();
//        params.height = getStatusBarHeight();

        mTextMessage = (TextView) findViewById(R.id.message);
//        thead.start();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Button btnCommit = (Button) findViewById(R.id.commit);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"asd",Toast.LENGTH_LONG).show();
                TextView txtUsr = (TextView) findViewById(R.id.txt);
                MainActivity.this.userName = txtUsr.getText().toString();
                new Thread(MainActivity.this.run).start();
            }
        });
    }

}
