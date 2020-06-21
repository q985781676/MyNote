package com.example.android.mynote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    //###############################################################
    //UI控件


    //获取当前mainActivity的引用
    private Context mainContext;
    //侧滑layout的引用
    private DrawerLayout drawerlayout;
    private ListView listView;   //主页面的list
    private ListView listView2;  //侧滑的list
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private MyBaseAdapter2 myBaseAdapter2;   //侧滑栏的list的adapter
    //private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;

    private List<Map<String,?>> inf;
    private List<String> inf2;
//###############################################################


    public static int ifLogin=0;  //用来控制是否已登录,登录则为1

    //登录后使用下面这两句来去掉登录选项,同时让静态变量ifLogin设为1
//     inf2.remove("登录");
//        myBaseAdapter2.notifyDataSetChanged();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View viewById = findViewById(R.id.fragment);

        mainContext=this;

        //初始化常用的layout
        InitLayout();
        //初始化listView
        InitList();


        //返回键
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }



        //使用NavigationUI对导航栏回退进行显示，无功能
        navController = Navigation.findNavController(viewById);
        NavigationUI.setupActionBarWithNavController(this,navController);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                                if (drawerlayout.isDrawerOpen(GravityCompat.START)){
                    drawerlayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerlayout.openDrawer(GravityCompat.START);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 实现navigation的回退
     */
    @Override
    public boolean onSupportNavigateUp() {
        //处理回退键盘显示问题
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.fragment).getWindowToken(),0);
        }

        navController.navigateUp();
        return super.onSupportNavigateUp();
    }


    public void InitLayout(){

        drawerlayout = findViewById(R.id.drawer_layout);
       // toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // toolbar.setNavigationIcon(R.drawable.support_black_24dp);


        //标题颜色
       // toolbar.setTitle("Title");
       // toolbar.setTitleTextColor(Color.BLACK);



//        drawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.app_name, R.string.app_name) {
//            @Override
//            public void onDrawerOpened(View drawerView) {//完全打开时触发
//                super.onDrawerOpened(drawerView);
//                Toast.makeText(MainActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {//完全关闭时触发
//                super.onDrawerClosed(drawerView);
//                Toast.makeText(MainActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
//            }
//
//
//        };
//
//
//        //toolbar设置的图标控制drawerlayout的侧滑
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerlayout.isDrawerOpen(GravityCompat.START)){
//                    drawerlayout.closeDrawer(GravityCompat.START);
//                } else {
//                    drawerlayout.openDrawer(GravityCompat.START);
//                }
//            }
//        });



    }

    public void InitList(){


        //初始化测试用的数据
        inf=new ArrayList<>();   //map

        for(int i=0;i<3;i++){
            Map<String, String> temp=new HashMap<>();
            temp.put("name","周末外出计划");
            temp.put("date","2019.5.23");
            temp.put("information","新京报快讯 据北京卫健委消息，6月18日0时至24时，新增报告本地确诊病例25例、疑似病例2例、无症状感染者2例。" +
                    "截至6月18日24时，累计报告本地确诊病例603例，累计出院411例，在院183例，累计死亡9例。尚在观察的无症状感染者15例。");

            inf.add(temp);

        }



        //使用myBaseAdapter
        //listView=findViewById(R.id.listView);
       // MyBaseAdapter myBaseAdapter=new MyBaseAdapter(inf);
       // listView.setAdapter(myBaseAdapter);



        inf2=new ArrayList<>();
        inf2.add("登录");
        inf2.add("同步");




        listView2=findViewById(R.id.listView2);
       myBaseAdapter2=new MyBaseAdapter2(inf2,this);
        listView2.setAdapter(myBaseAdapter2);



        //侧滑listView点击事件
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if((listView2.getItemAtPosition(position).toString())=="登录"){
                    //登录
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
                else if((listView2.getItemAtPosition(position).toString())=="同步"){
                    //同步


                }
            }
        });




    }

}
