package com.example.sharepreferences1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox rememberPass;
    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        accountEdit = (EditText) findViewById(R.id.accountEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        Button button1 = (Button) findViewById(R.id.button1);
        boolean isRemember = pref.getBoolean("remember_password", false);
//        从pref文件(SharePreference文件)读取是否勾上记住密码
        ListView contactsView = (ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);

        contactsView.setAdapter(adapter);
//        获得ListView实例和创建适配器和ListView控件关联；
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (account.equals("cjh") && password.equals("123456")) {
                    SharedPreferences.Editor editor = pref.edit();
                    if (rememberPass.isChecked()) {
                        editor.putString("account", account);
                        editor.putBoolean("remember_password", false);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, TextActivity1.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed,Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button addData = (Button) findViewById(R.id.add_db);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("Animal World");
                book.setAuthor("Chen");
                book.setPages(532);
                book.setPrice(1122);
                book.setPress("China Press");
                book.save();
                Book book2 = new Book();
                book.setName("ChenCun Noodles");
                book.setAuthor("Huang");
                book.setPages(386);
                book.setPrice(43);
                book.setPress("Food Press");
                book.save();
            }
        });
        Button updateData = (Button) findViewById(R.id.update_db);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("Computer Skill");
                book.setAuthor("HuNan");
                book.updateAll("author=?", "Chen");

            }
        });
        Button queryData = (Button) findViewById(R.id.query_db);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> bookList = LitePal.where("name like ? and price< ?", "chen%", "50").order("price").find(Book.class);
                for (Book book : bookList) {
                    Log.d("MainActivity", "Name:" + book.getName());
                    Log.d("MainActivity", "Author:" + book.getAuthor());
                }
            }
        });

        Button contacts = (Button) findViewById(R.id.contacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                        permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{Manifest.permission.READ_CONTACTS}, 1);
//                    读取联系人需要需要运行时权限；
                } else {
                    readContacts();
                }
            }
        });
    }


    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
