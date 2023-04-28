package com.example.icephonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean inputLegal() {
        return !TextUtils.isEmpty(registerNumber.getText()) && !TextUtils.isEmpty(registerPassword.getText());
    }

}