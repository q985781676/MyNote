package com.example.android.mynote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.android.mynote.R;
import com.example.android.mynote.entity.User;
import com.example.android.mynote.util.HttpUtil;
import com.example.android.mynote.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private FragmentActivity fragmentActivity;
    //组件
    private EditText mEtLoginUsername;
    private EditText mEtLoginPwd;
    private Button mBtLoginSubmit;
    private Button mBtLoginRegister;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //绑定全局 背景
        fragmentActivity = requireActivity();
        //初始化各控件
        initialUIComponent();
        //处理登录事件
        login();
        //初始化注册按钮导航
        toRegister();
    }

    private void initialUIComponent() {
        mEtLoginUsername = fragmentActivity.findViewById(R.id.et_login_username);
        mEtLoginPwd = fragmentActivity.findViewById(R.id.et_login_pwd);
        mBtLoginSubmit = fragmentActivity.findViewById(R.id.bt_login_submit);
        mBtLoginRegister = fragmentActivity.findViewById(R.id.bt_login_register);
    }

    /**
     * 登录事件
     */
    private void login() {
        //监听登录事件
        mBtLoginSubmit.setOnClickListener(v -> {
            //获取输入
            String userName = mEtLoginUsername.getText().toString();
            String password = mEtLoginPwd.getText().toString();
            //格式有问题结束此次点击
            if (verification(userName,password)){
                return;
            }
            //进行登录
            String res = "";
            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            try {
                res =  HttpUtil.postRequestJson(HttpUtil.BASE_URL+"/login",user);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //如果登录成功
            if (res.equalsIgnoreCase("true")) {
                //将信息保存到缓存 ,引入viewModel
                UserViewModel userViewModel = new ViewModelProvider(fragmentActivity).get(UserViewModel.class);
                userViewModel.setUserMutableLiveData(user);
                //跳转到主页
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_loginFragment_to_notesFragment);
            }
        });
    }
    //验证格式
    public boolean verification(String userName,String password){

        boolean flag = false;

        if (userName.isEmpty()) {
            flag = true;
            Toast.makeText(fragmentActivity,"用户名不能为空！",Toast.LENGTH_SHORT).show();
        }else if (password.length()<6){
            flag = true;
            Toast.makeText(fragmentActivity,"密码不能低于6位！",Toast.LENGTH_SHORT).show();
        }

        return flag;
    }
    /**
     * 初始化注册按钮功能
     */
    private void toRegister() {
        mBtLoginRegister.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }
}