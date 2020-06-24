package com.example.android.mynote.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.android.mynote.R;
import com.example.android.mynote.database.Note;
import com.example.android.mynote.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * 返回直接保存进数据库
 * 完成增加/修改操作
 */
public class EditorFragment extends Fragment implements EventListener {

    private final String TAG = "addTag";

    private EditText editText;
    private NoteViewModel noteViewModel;
    //键盘
    private InputMethodManager inputMethodManager;
    //返回当前activity
    FragmentActivity fragmentActivity;
    //是否是更新
    private Note note = null;
    //更新状态下记录原有的内容
    private String oldContent = null;
    //事件管理器（语音）
    private EventManager asr;

    public EditorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentActivity = requireActivity();
        //寻找操作对象
        editText = fragmentActivity.findViewById(R.id.note);
        FloatingActionButton btn = fragmentActivity.findViewById(R.id.btn);

        //获取note，判断本次是修改还是增加.注意添加默认值，否则无法以添加方式进入编辑页面
        note = (Note) (getArguments() != null ? getArguments().get("note") : null);
        //如果note非空，说明本次是修改
        if (note != null) {
            Log.d(TAG, "onActivityCreated: " + note);
            oldContent = note.getContent();
            editText.setText(oldContent);
        }


        //初始化ViewModel
        noteViewModel = new ViewModelProvider(fragmentActivity).get(NoteViewModel.class);

        /*
          弹出键盘
          1. 需要在manifest中的activity处添加android:windowSoftInputMode="adjustNothing",防止页面被压缩
          2. 大坑：必须先获取光标，再取键盘。否则先加载键盘会导致键盘无法弹出.
              解决：延迟一秒等绘制界面结束后再弹出
         */
        //获取光标
        editText.requestFocus();
        //初始化键盘
        inputMethodManager = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        //设定光标所在位置，大坑
        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                inputMethodManager.showSoftInput(editText, 0);
            }
        }, 1000);*/

        //获取权限
        initPermission();
        // 基于sdk集成1.1 初始化EventManager对象
        asr = EventManagerFactory.create(fragmentActivity, "asr");
        // 基于sdk集成1.3 注册自己的输出事件类
        asr.registerListener(this); //  EventListener 中 onEvent方法

        //设置触摸监听
        btn.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            //如果按下按钮，则开始识别
            if (action == MotionEvent.ACTION_DOWN) {
                start();
                Toast.makeText(getActivity(), "开始识别", Toast.LENGTH_SHORT).show();
            }//如果松开按钮，则停止识别
            else if (action == MotionEvent.ACTION_UP) {
                Toast.makeText(getActivity(), "识别结束", Toast.LENGTH_SHORT).show();
                //发送停止录音事件，提前结束录音等待识别结果
                asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
            }
            return false;
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //更新状态下记录提交前的内容
        String newContent = editText.getText().toString();
        String title;
        Log.d(TAG, "onPause: " + newContent);

        if (note != null) {
            //修改状态
            //1.先进行新旧内容比较
            if (oldContent.equals(newContent)) {
                Log.d(TAG, "onPause: 没有发生变化");
                return;
            }
            //2.发生变化，进行空值判断
            if (newContent.equals("")) {
                //如果文本内容已被清空,直接删除改id
                noteViewModel.deleteNotes(note);
                return;
            }
            //3. 进行修改
            //进行长度验证，选取合适部分给title赋值
            if (newContent.length() <= 16) {
                title = newContent;
            } else {
                title = newContent.substring(0, 16) + "...";
            }
            //设值修改
            note.setTitle(title);
            note.setContent(newContent);
            note.setLastUpdateTime(new Date().getTime());
            note.setFlag(1);
            noteViewModel.updateNotes(note);

        } else {
            /*
                添加状态
                对本次编辑内容进行验证，做出相应操作
                如果为空直接退出不进行任何操作,
             */
            if (!newContent.equals("")) {
                //进行长度验证，选取合适部分给title赋值
                if (newContent.length() <= 16) {
                    title = newContent;
                } else {
                    title = newContent.substring(0, 16) + "...";
                }

                //注入数据库note对象
                Note note = new Note();
                note.setTitle(title);
                note.setContent(newContent);
                note.setLastUpdateTime(new Date().getTime());
                note.setFlag(1);
                Log.d(TAG, "onPause: " + note);
                noteViewModel.insertNotes(note);
            }
        }
    }


    /**
     * 回调事件在EventListener中获取。
     * @param name      输出事件名
     * @param params    该事件的参数
     * @param data      额外数据
     * @param offset    额外数据
     * @param length    额外数据
     */
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        if (SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL.equals(name)) {
            // 识别相关的结果都在这里CALLBACK_EVENT_ASR_PARTIAL
            try {
                // 结果类型result_type(临时结果partial_result，最终结果final_result)
                // best_result最佳结果参数
                //在2.2熟悉源码的2.2.1设置识别/唤醒输入参数中可查看原始Json
                JSONObject object = new JSONObject(params);
                String result = object.getString("best_result");
                String resultType = object.getString("result_type");
                // 判断识别是否结束
                if ("final_result".equals(resultType)) {
                    editText.append(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        inputMethodManager.showSoftInput(editText, 0);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }*/

    //开始识别语音
    @SuppressLint("HandlerLeak")
    private void start() {
        Map<String, Object> params = new LinkedHashMap<>();//设置识别参数
        String event = SpeechConstant.ASR_START; // 替换成测试的event

        // 基于SDK集成2.1 设置识别参数
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        //此段可以自动检测错误
        boolean enableOffline = false;
        (new AutoCheck(fragmentActivity, new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        Log.d(TAG, "handleMessage:语音错误信息 "+message);
                    }
                }
            }
        }, enableOffline)).checkAsr(params);
        String json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);//发送事件
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(requireActivity(), perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), toApplyList.toArray(tmpList), 123);
        }
    }
}
