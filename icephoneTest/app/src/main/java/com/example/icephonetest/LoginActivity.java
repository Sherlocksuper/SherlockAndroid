package com.example.icephonetest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText numberIn;
    EditText passwordIn;
    boolean success;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        numberIn = findViewById(R.id.numberIn);
        passwordIn = findViewById(R.id.passwordIn);

        setTextListener();
        setLoginListener();
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
                    startActivity(intent);
                    finish();
                } else if (inputLegal() == false) {
                    Toast.makeText(LoginActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean inputLegal() {

        if (TextUtils.isEmpty(numberIn.getText()) || TextUtils.isEmpty(passwordIn.getText())) {
            return false;
        }
        return true;
    }


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

