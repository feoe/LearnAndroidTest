package com.example.sharepreferences1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TextActivity1 extends AppCompatActivity {
    private EditText editText2;
    private EditText editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button button2 = (Button) findViewById(R.id.button2);
        editText2 = (EditText) findViewById(R.id.accountEdit2);
        editText3 = (EditText) findViewById(R.id.passwordEdit2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String account = pref.getString("account", "");
                String password = pref.getString("password", "");
                editText2.setText(account);
                editText3.setText(password);
            }
        });
    }

}
