package com.example.icephonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountsManageActivity extends AppCompatActivity {
    ImageView backRows;

    public TextView resetPassword;
    public TextView resetLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsmanage);
        initView();
        initListener();
    }

    public void initView() {
        backRows = findViewById(R.id.accountsmanager_backRow);

        resetPassword = findViewById(R.id.reset_resetPassword);
        resetLogout = findViewById(R.id.reset_logout);
    }

    public void initListener() {
        backRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resetLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences(UsersCounts.usersCount,MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                editor.putBoolean("hasLogged",false);
                editor.apply();

                Intent intent = new Intent(AccountsManageActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountsManageActivity.this, ResetActivity.class);
                startActivity(intent);
            }
        });
    }
}