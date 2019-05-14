package com.example.xueyetong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import org.xyt.entity.User_info;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends BaseActivity {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private EditText editText_account;

    private EditText editText_password;

    private Button button_login;

    private CheckBox rememberPass;

    private String ip = "192.168.164.79";

    private Handler mHandler;

    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        editText_account = findViewById(R.id.account);
        editText_password = findViewById(R.id.password);
        button_login = findViewById(R.id.login);
        rememberPass = findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            editText_account.setText(account);
            editText_password.setText(password);
            rememberPass.setChecked(true);
        }

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                User_info user = (User_info) msg.obj;

                if (user.getUId().equals("fail")){
                    Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                } else if (!user.getUPassword().trim().equals(editText_password.getText().toString().trim())){
                    Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,"成功",Toast.LENGTH_SHORT).show();
                }
            }
        };

        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String userId = editText_account.getText().toString().trim();
                        User_info user = login(userId);
//                        Log.d(TAG, "here to check: "+user.getUId());
                        Message msg = Message.obtain();
                        msg.obj = user;
                        mHandler.sendMessage(msg);
                    }
                }).start();


//                String account = editText_account.getText().toString();
//                String password = editText_password.getText().toString();
//
//                if(account.equals("admin")&&password.equals("123456")){
//                    editor = pref.edit();
//                    if(rememberPass.isChecked()){
//                        editor.putBoolean("remember_password",true);
//                        editor.putString("account",account);
//                        editor.putString("password",password);
//                    }else {
//                        editor.clear();
//                    }
//                    editor.apply();
//
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else {
//                    Toast.makeText(LoginActivity.this,"account or password is invalid",
//                            Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public User_info login (String userId) {
//        Log.d(TAG, "here to check: "+userId);
        User_info result = null;
        URL url = null;
        try {
            url = new URL("http://"+ip+":8080/XYT_server/UserLogin");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            DataOutputStream outobj = new DataOutputStream(connection.getOutputStream());
            outobj.writeUTF(userId);
            outobj.flush();
            outobj.close();

            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            result = (User_info) ois.readObject();
            ois.close();
            connection.disconnect();
        } catch (Exception e) {
//            Log.d(TAG, "here to check3: "+e.toString());
            e.printStackTrace();
        }

        return result;
    }
}
