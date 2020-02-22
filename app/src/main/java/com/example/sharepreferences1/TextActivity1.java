package com.example.sharepreferences1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TextActivity1 extends AppCompatActivity implements View.OnClickListener {
    private EditText editText2;
    private EditText editText3;
    private TextView httpText;


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
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://www.bing.com")
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            showResponse(responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

        }
    }

    private void showResponse(final String responseData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                httpText.setText(responseData);
            }
        });
    }
}
