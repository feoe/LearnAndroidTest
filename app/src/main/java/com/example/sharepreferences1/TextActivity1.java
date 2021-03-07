package com.example.sharepreferences1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    private TextView jsonText;
    private Button startService;
    private Button stopService;
    private Button changeText;
    public static final int UPDATE_TEXT = 1;

    private TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button rememberAP = (Button) findViewById(R.id.rememberAP);
        editText2 = (EditText) findViewById(R.id.accountEdit2);
        editText3 = (EditText) findViewById(R.id.passwordEdit2);
        Button showHttp = (Button) findViewById(R.id.showHttp);
        Button showJson = (Button) findViewById(R.id.showJSON);
        showHttp.setOnClickListener(this);
        httpText = (TextView) findViewById(R.id.ResponseText);
        jsonText = (TextView) findViewById(R.id.JSONText);
        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        changeText = (Button) findViewById(R.id.change_text);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        目的是新建Message发送识别码给Handler主线程处理
                        Message message = new Message();
                        message.what = UPDATE_TEXT;

                    }
                }).start();
                break;
            case R.id.rememberAP:
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String account = pref.getString("account", "");
                String password = pref.getString("password", "");
                editText2.setText(account);
                editText3.setText(password);
                break;
            case R.id.showHttp:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
//                            发送请求均需要设定Client和Request对象
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://10.0.2.2/get_data.xml")
                                    .build();
//                            根据请求码发送，返回Response对象就是获取的信息，execute需要抛出异常代码
                            Response response = okHttpClient.newCall(request).execute();
//                            将数据转换成String形式
                            String jsonData = response.body().string();
                            showResponse(jsonData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.showJSON:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://10.0.2.2/get_data.json")
                                    .build();
//                            根据请求码发送，返回Response对象就是获取的信息，execute需要抛出异常代码
                            Response jsonResponse = okHttpClient.newCall(request).execute();
//                            将数据转换成String形式
                            String jsonData = jsonResponse.body().string();
                            showJson(jsonData);
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

    private void showJson(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.toString();
                String version = jsonObject.toString();
                String name = jsonObject.toString();
                Log.d(TAG, "id: " + id);
                Log.d(TAG, "version: " + version);
                Log.d(TAG, "name: " + name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showResponse(final String jsonData) {
        try {
//            构造出XmlPullParser实例
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
//            将服务器返回的XML数据设置进去xmlPullParser就可以开始解析，数据类型是String
            xmlPullParser.setInput(new StringReader(jsonData));
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
//              发现节点名等于id、name或version，就调用nextText() 方法来获取节点内具体的内容
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
