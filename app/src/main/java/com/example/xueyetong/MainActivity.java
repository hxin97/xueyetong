package com.example.xueyetong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyetong.Fragments.CommunityFragment;
import com.example.xueyetong.Fragments.HomeFragment;
import com.example.xueyetong.Fragments.NewsFragment;
import com.example.xueyetong.Fragments.NewsFragment_1;
import com.example.xueyetong.Fragments.NewsFragment_2;

public class MainActivity extends BaseActivity
        implements
        NewsFragment_1.OnFragmentInteractionListener,
        NewsFragment_2.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        CommunityFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    public boolean isLogin = false;
    private TextView login;
    private TextView register;
    private TextView logout;
    private TextView nav_userName;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    Fragment home;
    Fragment news;
    Fragment community;
    NavigationView navigationView;
    View headerLayout;
    DrawerLayout drawer;
    FloatingActionButton fab;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);
        Toolbar toolbar = findViewById(R.id.toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        fragmentManager = getSupportFragmentManager();
        home = new HomeFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content,home);
        transaction.commit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initNav_header();

        initNewsPages();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initNav_header();
        initNewsPages();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.tab_home:
                hindAllFragment();
                if (home != null) {
                    transaction.show(home);
                } else {
                    home = new HomeFragment();
                    transaction.add(R.id.content,home);

                }
                transaction.commit();
                break;
            case R.id.tab_news:
                hindAllFragment();
                if (news != null) {
                    transaction.show(news);
                }
                else {
                    news = new NewsFragment();
                    transaction.add(R.id.content,news);
                }
                transaction.commit();
                break;
            case R.id.tab_community:
                hindAllFragment();
                if (community != null) {
                    transaction.show(community);
                }
                else {
                    community = new CommunityFragment();
                    transaction.add(R.id.content,community);
                }
                transaction.commit();
                break;


        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hindAllFragment(){
        transaction = fragmentManager.beginTransaction();
        if (home != null){
            transaction.hide(home);
        }
        if (news != null) {
            transaction.hide(news);
        }
        if (community != null) {
            transaction.hide(community);
        }
    }

    public void initNav_header(){
        pref = getSharedPreferences("loginState", Context.MODE_PRIVATE);
        if(pref.getInt("state",0)==1){
            isLogin = true;
        } else { isLogin = false;}
        /**
         * 如果状态为已登录，将navigationView的headerLayout设置为其中一个样式，否则为另一样式
         */
        if( isLogin ) {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main2);
            nav_userName = (TextView)headerLayout.findViewById(R.id.nav_userName);
            nav_userName.setText(pref.getString("userName",""));
            logout = (TextView)headerLayout.findViewById(R.id.nav_logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nav_userName.setText("");
                    editor = pref.edit();
                    editor.putInt("state",0);
                    editor.apply();
                    drawer.closeDrawer(GravityCompat.START);
                    navigationView.removeHeaderView(navigationView.getHeaderView(0));
                    headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main1);
                    login = (TextView) headerLayout.findViewById(R.id.login);
                    register = (TextView)headerLayout.findViewById(R.id.register);
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });

        } else {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main1);
            login = (TextView) headerLayout.findViewById(R.id.login);
            register = (TextView)headerLayout.findViewById(R.id.register);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void initNewsPages(){}

    public void exit(){
        if((System.currentTimeMillis() - exitTime) > 2000){
            Toast.makeText(getApplicationContext(),"再按一次返回桌面",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
