package com.example.xueyetong;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xyt.entity.User_info;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText edit_userName;
    EditText edit_userId;
    RadioGroup radioGroup;
    EditText edit_university;
    EditText edit_college;
    EditText edit_className;
    EditText edit_phoneNum;
    EditText edit_password;
    EditText check_password;
    ImageView checkIdImage;
    TextView textView_err;
    Button reg;
    int userSex = 2; //性别默认为保密

    private String TAG = "RegisterActivity";

    private Handler mHandler;
    private Handler mHandler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edit_userName = findViewById(R.id.userName);
        edit_userId = findViewById(R.id.userId);
        radioGroup = findViewById(R.id.sex);
        edit_university = findViewById(R.id.university);
        edit_college = findViewById(R.id.college);
        edit_className = findViewById(R.id.className);
        edit_phoneNum = findViewById(R.id.phone_num);
        edit_password = findViewById(R.id.password);
        check_password = findViewById(R.id.check_password);
        checkIdImage = findViewById(R.id.check_id_image);
        textView_err = findViewById(R.id.textView_err);
        reg = findViewById(R.id.reg);
        reg.setEnabled(false); //初始化不可点击，在确认密码一栏填写完成后才设为可点击

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int result = msg.what;
//                Log.d(TAG, "here to check2:"+result);
                if(result==1){
                    if(!reg.isEnabled()){
                        reg.setEnabled(true);
                    }
                    checkIdImage.setImageResource(R.drawable.pass);
                } else if (result == 0){
                    if(reg.isEnabled()){
//                        Log.d(TAG, "handleMessage: enable");
                        reg.setEnabled(false);
                    }
                    checkIdImage.setImageResource(R.drawable.attention);
                    textView_err.setText("该学号已被注册过了");
                } else if (result == 2){
                    if(reg.isEnabled()){
                        reg.setEnabled(false);
                    }
                    Toast.makeText(RegisterActivity.this,"无法获取服务,请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        };

        mHandler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else if (msg.what == 0){
                    textView_err.setText("无法访问服务器，请检查网络状态");
                }
            }
        };

        radioGroup.setOnCheckedChangeListener(listener);

        /**
         * 输入用户id后连接服务器查询id是否已存在
         */
        edit_userId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    final String str = edit_userId.getText().toString().trim();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int examine = examine(str);
//                          Log.d(TAG, "here to check1: "+str);
                            Message msg = new Message();
                            msg.what = examine;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }

            }
        });

        check_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().equals(edit_password.getText().toString().trim())){
                    check_password.setBackgroundColor(getResources().getColor(R.color.edit_underline_attention));

                } else {
                    check_password.setBackgroundColor(getResources().getColor(R.color.white));
                    reg.setEnabled(true);
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = edit_userId.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                String userName = edit_userName.getText().toString().trim();
                if(userId.equals("")||password.equals("")||userName.equals("")){
                    Toast.makeText(RegisterActivity.this,"您还有必填信息未完善",Toast.LENGTH_SHORT).show();
                }else {


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User_info newUser = new User_info();
                            newUser.setUId(edit_userId.getText().toString().trim());
                            newUser.setUPassword(edit_password.getText().toString().trim());
                            newUser.setUName(edit_userName.getText().toString().trim());
                            newUser.setUSex(userSex);
                            newUser.setUUniversity(edit_university.getText().toString().trim());
                            newUser.setUCollege(edit_college.getText().toString().trim());
                            newUser.setUClass(edit_className.getText().toString().trim());
                            newUser.setUPhoneNum(edit_phoneNum.getText().toString().trim());
                            newUser.setUPhoto("");
                            int result = register(newUser);
                            Message msg = new Message();
                            msg.what = result;
                            mHandler2.sendMessage(msg);
                        }
                    }).start();

                }
            }
        });
    }


    public int register(User_info user){
        URL url;
        try {
            url = new URL("http://"+LoginActivity.ip+":8080/XYT_server/UserRegister");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            ObjectOutputStream outobj = new ObjectOutputStream(connection.getOutputStream());
            outobj.writeObject(user);
            outobj.flush();
            outobj.close();


            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String result = bufferedReader.readLine();
            Log.d(TAG, "register result: "+result);

            inputStreamReader.close();


            connection.disconnect();
        } catch (Exception e) {
            Log.d(TAG, "here to check3: "+e.toString());
            e.printStackTrace();
            return 0;
        }
        return 1;
    }


    public int examine (String userId) {

//        Log.d(TAG, "here to check: "+userId);

        StringBuilder result = new StringBuilder();
        InputStream is;

        try {
            URL url = new URL("http://"+LoginActivity.ip+":8080/XYT_server/UserLogin?userId="+userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"utf-8"));

                result.setLength(0);
                result.append(bufferedReader.readLine());
                is.close();
            }
//            Log.d(TAG, "here to check: "+result.toString());
            connection.disconnect();
            if(result.toString().equals("success")){
                return 1;
            }
            else if(result.toString().equals("existed")){
                return 0;
            }



        } catch (Exception e) {
            Log.d(TAG, "here to check3: "+e.toString());
            e.printStackTrace();
            return 2;
        }

        return 2;
    }


    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.male:
                    userSex = 0;
                    Log.d(TAG, "onCheckedChanged: "+userSex);
                    break;
                case R.id.female:
                    userSex = 1;
                    Log.d(TAG, "onCheckedChanged: "+userSex);
                    break;
                case R.id.unknown:
                    userSex = 2;
                    break;
                default:
                    userSex = 2;
                    break;
            }
        }
    };

}
