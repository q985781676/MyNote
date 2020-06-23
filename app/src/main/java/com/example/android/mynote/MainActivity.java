package com.example.android.mynote;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.android.mynote.entity.User;
import com.example.android.mynote.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private NavController navController;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View viewById = findViewById(R.id.fragment);

        //使用NavigationUI对导航栏回退进行显示，无功能
        navController = Navigation.findNavController(viewById);
        NavigationUI.setupActionBarWithNavController(this,navController);

        //testPostForm();
        //testPostJson();

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testPostForm(){
        Map<String,String> map = new HashMap<>();
        map.put("name","zs");
        map.put("password","123");
        try {
            String postRequest = HttpUtil.postRequestForm(HttpUtil.BASE_URL+"/test", map);
            Log.d("my_activity", "onCreate: "+postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testPostJson(){
        User user = new User();
        user.setUserName("ls");
        user.setPassword("123456");
        try {
            String postRequest = HttpUtil.postRequestJson(HttpUtil.BASE_URL+"/testPostJson",user);
            Log.d("my_activity", "onCreate: "+postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
