package com.example.android.mynote.fragment;

import android.os.Bundle;
import android.util.Log;
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
public class RegisterFragment extends Fragment {

    private FragmentActivity fragmentActivity;
    //ui控件
    private EditText mEtRegisterUsername;
    private EditText mEtRegisterPwd;
    private EditText mEtRegisterConfirmPwd;
    private Button mBtRegisterSubmit;



    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentActivity = requireActivity();
        //初始化控件
        initialUIComponent();
        //注册提交
        registerAccount();
    }

    public void initialUIComponent(){
        mEtRegisterUsername = fragmentActivity.findViewById(R.id.et_register_username);
        mEtRegisterPwd = fragmentActivity.findViewById(R.id.et_register_pwd_input);
        mEtRegisterConfirmPwd = fragmentActivity.findViewById(R.id.et_register_pwd_input_confirm);
        mBtRegisterSubmit = fragmentActivity.findViewById(R.id.bt_register_submit);
    }

    public void registerAccount(){
        mBtRegisterSubmit.setOnClickListener(v -> {
            //获取输入
            String userName = mEtRegisterUsername.getText().toString();
            String password = mEtRegisterPwd.getText().toString();
            String confirmPassword = mEtRegisterConfirmPwd.getText().toString();
            //验证格式
            if (verification(userName,password,confirmPassword)) {
                Log.d("register", "registerAccount: ver false");
                return;
            }
            //进行注册
            String res = "";
            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            try {
                res =  HttpUtil.postRequestJson(HttpUtil.BASE_URL+"/user",user);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //如果提交成功
            if (res.equalsIgnoreCase("true")) {
                Log.d("register", "registerAccount: "+res);
                //将信息保存到缓存 ,引入viewModel
                UserViewModel userViewModel = new ViewModelProvider(fragmentActivity).get(UserViewModel.class);
                userViewModel.setUserMutableLiveData(user);
                //跳转到主页
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_registerFragment_to_notesFragment);
            }
        });
    }

    //验证格式
    public boolean verification(String userName,String password,String confirmPassword){

        boolean flag = false;

        if (userName.isEmpty()) {
            flag = true;
            Toast.makeText(fragmentActivity,"用户名不能为空！",Toast.LENGTH_SHORT).show();
        }else if (duplicateUserName(userName).equalsIgnoreCase("true")){
            flag = true;
            Toast.makeText(fragmentActivity,"用户名已存在！",Toast.LENGTH_SHORT).show();
        }else if (password.length()<6){
            flag = true;
            Toast.makeText(fragmentActivity,"密码不能低于6位！",Toast.LENGTH_SHORT).show();
        }else if (!confirmPassword.equalsIgnoreCase(password)){
            flag = true;
            Toast.makeText(fragmentActivity,"两次密码输入必须一致！",Toast.LENGTH_SHORT).show();
        }

        return flag;
    }

    /**
     * 用户名查重
     */
    public String duplicateUserName(String userName){
        String request = null;
        try {
             request = HttpUtil.getRequest(HttpUtil.BASE_URL + "/user/" + userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}