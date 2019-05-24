package com.example.xueyetong;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xyt.entity.Activity_info;

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

        sharedPreferences = getSharedPreferences("loginState", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("state",0) == 1){
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

                                    int result = signUp();

                                    switch (result){
                                        case 0:
                                            dialog.dismiss();
                                            failDialog.show();
                                            break;
                                        case 1:
                                            dialog.dismiss();
                                            successDialog.show();
                                            break;
                                        case 2:
                                            dialog.dismiss();
                                            errorDialog.show();
                                            break;
                                        default:
                                            dialog.dismiss();
                                            break;
                                    }
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

    public int signUp(){

        return 0;
    }
}
