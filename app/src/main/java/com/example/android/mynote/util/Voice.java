package com.example.android.mynote.util;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.android.mynote.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

// 继承百度内部的监听
public class Voice {// implements EventListener{
   /* protected EditText Result;
    protected Button btn;
    // 设置事件管理器
    private EventManager asr;

    *//*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add);

        // 动态申请权限
        initPermission();

        // 基于sdk集成1.1 初始化EventManager对象 设置自定义监听
        asr = EventManagerFactory.create(this, "asr");
        // 基于sdk集成1.3 注册自己的输出事件类
        asr.registerListener(this);

        Result = findViewById(R.id.note);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),"开始识别",Toast.LENGTH_LONG);
                toast.show();
                start();
            }
        });
    }*//*

    *//**
     * 基于SDK集成2.2 发送开始事件
     * 点击开始按钮
     *//*
    private void start(){
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        // 基于SDK集成2.1 设置识别参数
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        // 基于SDK集成2.2 发送start开始事件
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    *//*
     *基于sdk集成1.2 自定义输出事件类
     *EventListener 回调方法
     *//*
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        if (SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL.equals(name)){
            // 识别相关的结果都在这里CALLBACK_EVENT_ASR_PARTIAL
            try {
                // 结果类型result_type(临时结果partial_result，最终结果final_result)
                // best_result最佳结果参数
                //在2.2熟悉源码的2.2.1设置识别/唤醒输入参数中可查看原始Json
                JSONObject object = new JSONObject(params);
                String result = object.getString("best_result");
                String resultType = object.getString("result_type");
                // 判断识别是否结束
                if ("final_result".equals(resultType)){
                    Result.setText(result);
                    Toast toast = Toast.makeText(getApplicationContext(),"识别已结束",Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    *//**
     * android 6.0 以上需要动态申请权限
     * (以下可直接从ActivityMiniRecog类中复制)
     *//*
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }

    *//*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }*//*
*/
}
