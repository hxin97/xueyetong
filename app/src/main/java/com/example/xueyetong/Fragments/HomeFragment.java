package com.example.xueyetong.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyetong.ActivityAdapter;
import com.example.xueyetong.R;

import org.xyt.entity.Activity_info;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.xueyetong.LoginActivity.ip;

public class HomeFragment extends Fragment {

    private String TAG = "HomeFragment";

    private OnFragmentInteractionListener mListener;
    private Handler mHandler;
    private Handler handler1;
    private ArrayList<Activity_info> activityInfos;
    private ActivityAdapter adapter;
    RecyclerView recyclerView;
    TextView home_info;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = (Handler) new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                activityInfos = (ArrayList<Activity_info>) msg.obj;
                Message msg1 = new Message();
                if (activityInfos.size()==0){
                    //数据库查询不到
                    msg1.what = 2;
                }
                else if (activityInfos.get(0).getTitle().equals("network error")){
                    //网络原因无法访问服务器
//                    Log.d(TAG, "handleMessage: here to check network error");
                    msg1.what = 0;
                }
                else {
                    msg1.what = 1;
                }
                handler1.sendMessage(msg1);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Activity_info> activity_infos = getActivityInfo();
                Message msg = new Message();
                msg.obj = activity_infos;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        home_info = view.findViewById(R.id.home_info);
        recyclerView = view.findViewById(R.id.activity_rec_view);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ActivityAdapter(new ArrayList<Activity_info>()));

        handler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==2){
                    //数据库查询不到
                    home_info.setText("无内容可显示");
                }
                else if (msg.what == 0){
                    //网络原因无法访问服务器
//                    Log.d(TAG, "handleMessage: here to check network error");
                    home_info.setText("无法访问服务器，请检查网络");
                }
                else if (msg.what == 1){
                    adapter = new ActivityAdapter(activityInfos);
                    recyclerView.setAdapter(adapter);
                }
            }
        };

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public ArrayList<Activity_info> getActivityInfo(){

        ArrayList<Activity_info> activityList ;

        try {
            URL url = new URL("http://"+ip+":8080/XYT_server/GetActivity");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
                activityList = (ArrayList) ois.readObject();
                ois.close();
                connection.disconnect();
                Log.d(TAG, "getActivityInfo: here to check what: "+activityList.get(0).getTitle());
                return activityList;
            }

        } catch (Exception e) {
            Log.d(TAG, "getActivityInfo: here to check"+e.toString());
            activityList = new ArrayList<Activity_info>();
            Activity_info errInfo = new Activity_info();
            errInfo.setTitle("network error");
            activityList.add(errInfo);
            return activityList;

        }

        activityList = new ArrayList<Activity_info>();
        Activity_info errInfo = new Activity_info();
        errInfo.setTitle("network error");
        activityList.add(errInfo);
        return activityList;

    }
}
