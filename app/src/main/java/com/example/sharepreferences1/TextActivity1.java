package com.example.sharepreferences1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TextActivity1 extends AppCompatActivity implements View.OnClickListener {
    private EditText editText2;
    private EditText editText3;
    private TextView httpText;
    private static final String TAG = "TextActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button rememberAP = (Button) findViewById(R.id.rememberAP);
        editText2 = (EditText) findViewById(R.id.accountEdit2);
        editText3 = (EditText) findViewById(R.id.passwordEdit2);
        Button showhttp = (Button) findViewById(R.id.ShowHttp);
        showhttp.setOnClickListener(this);
        httpText = (TextView) findViewById(R.id.ResponseText);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rememberAP:
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String account = pref.getString("account", "");
                String password = pref.getString("password", "");
                editText2.setText(account);
                editText3.setText(password);
                break;
            case R.id.ShowHttp:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
//                            发送请求均需要设定Client和Request变量对象
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://10.0.2.2/get_data.xml")
                                    .build();
//                            根据请求码发送，返回Response对象就是获取的信息，execute需要抛出异常代码
                            Response response = okHttpClient.newCall(request).execute();
//                            将数据转换成String形式
                            String responseData = response.body().string();
                            showResponse(responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            default:
                break;
        }
    }

    private void showResponse(final String responseData) {
        try {
//            构造出XmlPullParser实例
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
//            将服务器返回的XML数据设置进去xmlPullParser就可以开始解析，数据类型是String
            xmlPullParser.setInput(new StringReader(responseData));
//            重点：根据XmlPullParser生成eventType，根据事件不同处理
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
//               获得当前事件的名称， .getName返回节点名称，String类型
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
//               发现节点名等于id、name或version，就调用nextText() 方法来获取节点内具体的内容
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d(TAG, "id is " + id);
                            Log.d(TAG, "name is " + name);
                            Log.d(TAG, "version is " + version);

                        }
                        break;

                    }
                    default:
                        break;
                }

                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
