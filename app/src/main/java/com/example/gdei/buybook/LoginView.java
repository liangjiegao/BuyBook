package com.example.gdei.buybook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by gdei on 2018/5/9.
 */

public class LoginView extends Activity {
    private static final String TAG = "LoginView";

    private RelativeLayout login_main_rl;
    private EditText login_et_userName, login_et_password, host, post;
    private Button login_bt_login;

    //记录用户输入的账号密码
    private String userName, password;
    private BufferedReader br;  //服务器响应的数据
    //处理发送登录、注册请求等请求， 并 处理返回数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //判断登录请求
            if (msg.what == 0x123) {
                String str = "";
                StringBuffer result = new StringBuffer();
                try {
                    while ((str = br.readLine()) != null) {
                        result.append(str);
                    }
                    System.out.println(result.toString());
                    if (result.toString().equals("账号不存在！")) {
                        Toast.makeText(LoginView.this, "账号不存在！！", Toast.LENGTH_LONG).show();
                    } else {
                        if (result.toString().equals("密码错误！")) {
                            Toast.makeText(LoginView.this, "密码错误！！", Toast.LENGTH_LONG).show();
                        } else {
                                Intent toMainView = new Intent(LoginView.this, MainActivity.class);
                                User.setUserName(userName);
                                User.setPassword(password);
                                Log.i(TAG, "handleMessage: username"+userName);
                                startActivity(toMainView);
                                finish();

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (msg.what == 0x234){
                Toast.makeText(LoginView.this, "登录失败！！", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    /**
     * 初始化界面
     */
    public void initView() {
        login_main_rl = findViewById(R.id.login_main_rl);
        login_et_userName = findViewById(R.id.login_et_userName);
        login_et_password = findViewById(R.id.login_et_password);
        login_bt_login = findViewById(R.id.login_bt_login);


        login_bt_login = findViewById(R.id.login_bt_login);

        login_bt_login.setOnClickListener(new MyListener());

    }

    /**
     * 处理登录业务
     */
    public void login() {
        //获取账号密码输入信息
        userName = login_et_userName.getText().toString();
        password = login_et_password.getText().toString();
        //如果有输入且输入不为空
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            try {
                //判断密码是否纯数字
                Integer.parseInt(password);
            } catch (Exception e) {
                Toast.makeText(LoginView.this, "密码格式有误！！", Toast.LENGTH_LONG).show();
                return;
            }
            new Thread(){
                @Override
                public void run() {
                    LoginThread loginThread = new LoginThread(userName, password);
                    FutureTask<ArrayList<Book>> task = new FutureTask<ArrayList<Book>>(loginThread);
                    new Thread(task).start();
                    try {
                        if (task.get() == null){
                            Log.i(TAG, "login: username"+userName+"password"+password);
                            handler.sendEmptyMessage(0x234);
                        }else {
                            Store.setBooks(task.get());
                            handler.sendEmptyMessage(0x123);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    handler.sendEmptyMessage(0x123);
                }
            }.start();

        } else {
            Toast.makeText(LoginView.this, "账号和密码都不能为空！！", Toast.LENGTH_LONG).show();
        }
    }

    private void getHostPost(){
        host = findViewById(R.id.host);
        post = findViewById(R.id.post);
        SocketUtil.setHost(host.getText().toString());
        SocketUtil.setPost(Integer.parseInt(post.getText().toString()));
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_bt_login:
                    login();
                    break;
            }
        }
    }
}
