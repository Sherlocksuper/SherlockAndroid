package com.example.icephonetest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText numberIn;
    EditText passwordIn;

    public UsersCounts usersCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        numberIn = findViewById(R.id.numberIn);
        passwordIn = findViewById(R.id.passwordIn);
        checkLogStatu();
        setTextListener();
        setLoginListener();
    }

    public void checkLogStatu() {
        SharedPreferences preferences = this.getSharedPreferences("over", MODE_PRIVATE);
        boolean hasLogged = preferences.getBoolean("hasLogged", false);
        UsersCounts.usersCount = preferences.getString("counts",0+"");

        Log.d("TAG", "checkLogStatu: "+hasLogged);
        if (hasLogged) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void setTextListener() {
        Intent toRegister = new Intent(this, RegisterActivity.class);
        TextView textView = findViewById(R.id.item_data);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toRegister);
            }
        });
    }

    //登录按钮监听
    public void setLoginListener() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputLegal()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    setCounts();
                    startActivity(intent);
                    finish();


//                  网络请求
//                  Toast.makeText(LoginActivity.this, "正在登陆，请稍候", Toast.LENGTH_SHORT).show();
//                  sendRequestWithOkHttp();
                } else if (!inputLegal()) {
                    Toast.makeText(LoginActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });


        numberIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputLegal()) {
                    // 输入合法，将登录按钮的背景颜色设置为蓝色
                    login.setBackgroundColor((Color.BLUE));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputLegal()) {
                    // 输入合法，将登录按钮的背景颜色设置为蓝色
                    login.setBackgroundColor((Color.BLUE));
                } else {
                    login.setBackgroundColor((Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public boolean inputLegal() {

        return !TextUtils.isEmpty(numberIn.getText()) && !TextUtils.isEmpty(passwordIn.getText());
    }


    public void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", numberIn.getText().toString());
                    jsonObject.put("password", passwordIn.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody requestBody = RequestBody.create(type, "" + jsonObject);

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://10.245.150.220:8082/User/login")
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
                    Toast.makeText(LoginActivity.this, "网络请求失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                String result = (String) msg.obj; // 获取返回的信息
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    Log.d("TAG ", "handleMessage: " + code);
                    if (code == 600) {
                        String errorMsg = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        // 对其他情况进行处理
                    } else if (code == 400) {
                        String errorMsg = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                        UsersCounts.usersCount = numberIn.getText().toString();
                        UsersCounts.usersPassword = passwordIn.getText().toString();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        SharedPreferences preferences = getSharedPreferences("over", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("hasLogged", true);
                        editor.putString("counts",UsersCounts.usersCount);
                        editor.apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setCounts() {
        UsersCounts.usersCount = numberIn.getText().toString();
    }
}

