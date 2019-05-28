package com.example.xueyetong;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xyt.entity.Activity_info;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.xueyetong.LoginActivity.ip;

public class DetailActivity extends BaseActivity {

    public static final String SELECTED_ACTIVITY = "selectedActivity";
    TextView tv_title;
    TextView tv_type;
    TextView tv_date;
    TextView tv_site;
    TextView tv_speaker;
    TextView tv_college;
    TextView tv_intro;
    TextView tv_id;
    Button sign_up;
    SharedPreferences sharedPreferences;
    Activity_info activity_info;
    AlertDialog.Builder errorDialog;
    AlertDialog.Builder successDialog;
    AlertDialog.Builder failDialog;
    int result;
    int haveSign;
    private String TAG = "DetailActivity";
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tv_title = findViewById(R.id.detail_tv_1);
        tv_type = findViewById(R.id.detail_tv_2);
        tv_date = findViewById(R.id.detail_tv_3);
        tv_site = findViewById(R.id.detail_tv_4);
        tv_speaker = findViewById(R.id.detail_tv_5);
        tv_college = findViewById(R.id.detail_tv_6);
        tv_intro = findViewById(R.id.detail_tv_7);
        tv_id = findViewById(R.id.detail_tv_8);
        sign_up = findViewById(R.id.sign_up);

        Intent intent = getIntent();
        activity_info = (Activity_info) intent.getSerializableExtra(SELECTED_ACTIVITY);

        tv_title.setText(activity_info.getTitle());
        tv_type.setText(activity_info.getActivityType());
        tv_date.setText(activity_info.getStartTime().substring(5,16)+" - "+activity_info.getEndTime().substring(11,16));
        tv_site.setText(activity_info.getSite());
        tv_speaker.setText(activity_info.getSpeaker());
        tv_college.setText(activity_info.getCollege());
        tv_intro.setText(activity_info.getIntro());
        tv_id.setText(activity_info.getActivityId()+"");

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int haveSign = msg.what;
                if (haveSign == 1) {
                    sign_up.setText("已成功报名");
                    sign_up.setEnabled(false);
                }
                else if (haveSign == 0) {
                    sign_up.setText("报名 （"+activity_info.getRemainingNum()+"/"+activity_info.getNum()+"）");
                    sign_up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                                    .setTitle("请确认信息")
                                    .setMessage("报名活动："+activity_info.getTitle()+"\n"
                                            +"报名学生学号："+sharedPreferences.getString("account",null))
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    result = signUp(sharedPreferences.getString("account", null), activity_info.getActivityId());
                                                    Log.d(TAG, "check result in detailac... = "+result);
                                                }
                                            }).start();

                                            dialog.dismiss();

                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    switch (result) {
                                                        case 0:
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    errorDialog.show();
                                                                }
                                                            });
                                                            break;
                                                        case 1:
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    successDialog.show();
                                                                    sign_up.setText("已成功报名");
                                                                    sign_up.setEnabled(false);
                                                                }
                                                            });

                                                            break;
                                                        case 2:
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    failDialog.show();
                                                                }
                                                            });

                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }
                                            }, 2000);

                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    });
                } else {
                    Toast.makeText(DetailActivity.this,"无法访问服务器",Toast.LENGTH_SHORT).show();
                }
            }
        };

        sharedPreferences = getSharedPreferences("loginState", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("state",0) == 1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    haveSign = ifHaveSign(sharedPreferences.getString("account", null), activity_info.getActivityId());
                    Message msg = new Message();
                    msg.what = haveSign;
                    mHandler.sendMessage(msg);
                }
            }).start();

        }
        else {
            sign_up.setText("登录后才能报名哦");
            sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        errorDialog= new AlertDialog.Builder(DetailActivity.this)
                .setTitle("错误").setMessage("无法访问服务器").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        successDialog= new AlertDialog.Builder(DetailActivity.this)
                .setTitle("提示").setMessage("报名成功。").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        failDialog= new AlertDialog.Builder(DetailActivity.this)
                .setTitle("提示").setMessage("名额已满！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


    }

    public int signUp(String userId, int activityId){

        int result = 0;

        try {
            URL url = new URL("http://"+ip+":8080/XYT_server/Orders");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            String content = "userId="+ URLEncoder.encode(userId, "UTF-8");
            content += "&activityId="+URLEncoder.encode(""+activityId, "UTF-8");

            out.writeBytes(content);

            out.flush();
            out.close();

            DataInputStream dis = new DataInputStream(connection.getInputStream());

            // 0表示出错，1表示成功报名，2表示名额已满。
            result = dis.readInt();

            dis.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        return result;
    }

    public int ifHaveSign(String userId, int activityId) {
        int result = 2;
        try {
            URL url = new URL("http://"+LoginActivity.ip+":8080/XYT_server/Orders?userId="+userId+"&activityId="+activityId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setRequestMethod("GET");
//            Log.d(TAG, "here to check: "+url.toString());
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = Integer.parseInt(br.readLine());
            connection.disconnect();
//            Log.d(TAG, "here to check result"+result);

        } catch (Exception e) {
            Log.d(TAG, "here to check result: "+result);
            e.printStackTrace();
        } finally {
            return result;
        }

    }
}
