package com.example.icephonetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    public TextView tx_goToLogin;

    public Button registerButton;
    public EditText registerNumber;
    public EditText registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initOnClickListener();
    }

    public void initView() {
        tx_goToLogin = findViewById(R.id.register_geToLogin);
        registerButton = findViewById(R.id.register);
        registerNumber = findViewById(R.id.register_number);
        registerPassword = findViewById(R.id.register_password);
    }

    public void initOnClickListener() {
        tx_goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputLegal()) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

//                  Toast.makeText(RegisterActivity.this, "正在注册，请稍后", Toast.LENGTH_SHORT).show();
//                  sendRequestWithOkHttp();


                } else {
                    Toast.makeText(RegisterActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputLegal()) {
                    // 输入合法，将登录按钮的背景颜色设置为蓝色
                    registerButton.setBackgroundColor((Color.BLUE));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputLegal()) {
                    // 输入合法，将登录按钮的背景颜色设置为蓝色
                    registerButton.setBackgroundColor((Color.BLUE));
                }else{
                    registerButton.setBackgroundColor((Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    //判断输入是否为空
    public boolean inputLegal() {
        return !TextUtils.isEmpty(registerNumber.getText()) && !TextUtils.isEmpty(registerPassword.getText());
    }
    public void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", registerNumber.getText().toString());
                    jsonObject.put("password", registerPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody requestBody = RequestBody.create(type, "" + jsonObject);

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://10.245.150.220:8082/User/register")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        handler.obtainMessage(1, response.body().string()).sendToTarget();
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "网络请求失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private final Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String result = (String) msg.obj; // 获取返回的信息
            try {
                JSONObject jsonObject = new JSONObject(result);
                int code = jsonObject.getInt("code");
                if (code == 400) { // 判断错误码是否为 400
                    String errorMsg = jsonObject.getString("message");
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    // 对错误信息进行处理，例如弹窗提示用户等等
                } else if (code == 700) {
                    String errorMsg = jsonObject.getString("message");
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    // 对其他情况进行处理
                } else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}