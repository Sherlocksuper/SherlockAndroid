package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.XMLFormatter;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private boolean success;
    private String token;
    EditText ed_account;
    EditText ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setGtRegAndCancelListenner();
        setLoginListenner();
        ed_account = findViewById(R.id.accountIn);
        ed_password = findViewById(R.id.passwordIn);
    }

    //登录页面
    //取消和跳转到注册页面的监听器
    public void setGtRegAndCancelListenner() {

        Intent toRegister = new Intent(this, RegisterActivity.class);

        TextView tv_goToRegister = findViewById(R.id.geToRegister);
        tv_goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toRegister);
            }
        });

        TextView tv_cancelLogin = findViewById(R.id.cancel_Login);
        tv_cancelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    //登录按钮监听
    public void setLoginListenner() {

        Button bv_login = findViewById(R.id.login);
        bv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputLegal()) {
                    sendLoginRequest();

                } else if (inputLegal() == false) {
                    Toast.makeText(LoginActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //判断输入是否为空
    public boolean inputLegal() {

        if (TextUtils.isEmpty(ed_account.getText()) || TextUtils.isEmpty(ed_password.getText())) {
            return false;
        }
        return true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 2) {
                String returnMessage = (String) msg.obj;
                final LoginActivity.LoginData loginData = new Gson().fromJson(returnMessage, LoginActivity.LoginData.class);
                success = loginData.isSuccess();
                token = loginData.getToken();
                Log.d("LoginActivity", "handleMessage: " + loginData.getMessage() + "  " + loginData.getToken() + " " + loginData.isSuccess());
                if (success) {
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                    //刷新一下登陆布局
                    MainActivity.instance.finish();
                    Log.d("LoginActivity", "handleMessage: " + token + "111111111111111");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    //共享数据储存登录状态
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putBoolean("hasLogged", true);
                    editor.putString("token", token);
                    editor.apply();
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "登陆失败，请检查账号密码是否正确", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };


    public void sendLoginRequest() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", ed_account.getText().toString());
                    jsonObject.put("password", ed_password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody requestBody = RequestBody.create(type, "" + jsonObject);

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://test.xiandejia.com:8888/douban_server/login")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        handler.obtainMessage(2, response.body().string()).sendToTarget();
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //login 工具类
    public class LoginData {
        public String message;
        public String data;
        public boolean success;

        public LoginData(String message, String token, boolean success) {
            this.message = message;
            this.data = token;
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return data;
        }

        public void setToken(String token) {
            this.data = token;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

}