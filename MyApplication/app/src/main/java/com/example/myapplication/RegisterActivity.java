package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.IdentityHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "RegisterActivity";
    Button bu_sign;
    String token;
    boolean hassign;
    String message;
    EditText ed_account;
    EditText ed_password;
    ImageView tv_cancel;
    boolean success;
    int flag = 0;

    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_account = findViewById(R.id.accountSet);
        ed_password = findViewById(R.id.passwordSet);
        bu_sign = findViewById(R.id.notlog_sign);
        bu_sign.setOnClickListener(this);

        tv_cancel = findViewById(R.id.register_goback);
        clickit();
    }

    @Override
    public void onClick(View view) {
        if (inputLegal()) {
            if (view.getId() == R.id.notlog_sign) {
                sendRequestWithOkHttp();
                //Toast.makeText(view.getContext(), success + "", Toast.LENGTH_SHORT).show();}
            }
        } else if (inputLegal() == false) {
            Toast.makeText(this, "输入不合法", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickit(){
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public boolean inputLegal() {
        if (TextUtils.isEmpty(ed_account.getText()) || TextUtils.isEmpty(ed_password.getText())) {
            return false;
        }
        return true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                String returnMessage = (String) msg.obj;
                final registerData registerData = new Gson().fromJson(returnMessage, registerData.class);
                success = registerData.isSuccess();

                if (success) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "该账号已被注册，请重新注册", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    private void sendRequestWithOkHttp() {
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
                            .url("https://test.xiandejia.com:8888/douban_server/signup")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        handler.obtainMessage(1, response.body().string()).sendToTarget();
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public class registerData {
        public String message;
        public String token;
        public boolean success;

        public registerData(String message, String token, boolean success) {
            this.message = message;
            this.token = token;
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

}

