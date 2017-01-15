package com.jay.bihu.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jay.bihu.R;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.NetWorkUtils;
import com.jay.bihu.view.LoginDialog;

import java.io.IOException;

public class LoginActivity extends BaseActivity {
    private LoginDialog mDialog;
    private TextInputLayout mUsernameWrapper;
    private TextInputLayout mPasswordWrapper;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        mDialog = new LoginDialog(this);
        initDialog();
    }

    private void initDialog() {
        mDialog.show();
        //注册
        Button register = (Button) mDialog.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameWrapper.getEditText().getText().toString();
                String password = mPasswordWrapper.getEditText().getText().toString();
                register(username, password);
            }
        });
        //登录
        Button login = (Button) mDialog.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameWrapper.getEditText().getText().toString();
                String password = mPasswordWrapper.getEditText().getText().toString();
                login(username, password);
            }
        });

        mUsernameWrapper = (TextInputLayout) mDialog.findViewById(R.id.usernameWrapper);
        mPasswordWrapper = (TextInputLayout) mDialog.findViewById(R.id.passwordWrapper);

        String username = mPreferences.getString("username", "");
        String password = mPreferences.getString("password", "");
        mUsernameWrapper.getEditText().setText(username);
        mPasswordWrapper.getEditText().setText(password);
        boolean isLogin = mPreferences.getBoolean("isLogin", false);
        if (isLogin && NetWorkUtils.isNetworkConnected(this))
            login(username, password);
    }

    private void register(String username, String password) {
        if (!NetWorkUtils.isNetworkConnected(this)) {
            showMessage("无网络连接");
            return;
        }

        if (username.length() < 1) {
            mUsernameWrapper.setError("用户名过短");
            return;
        }
        if (password.length() < 5) {
            mPasswordWrapper.setError("密码过短");
            return;
        }
        loginOrRegister(ApiConfig.REGISTER, username, password);
    }

    private void login(String username, String password) {
        if (!NetWorkUtils.isNetworkConnected(this)) {
            showMessage("无网络连接");
            return;
        }
        loginOrRegister(ApiConfig.LOGIN, username, password);
    }

    private void loginOrRegister(String address, String username, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登录中...");
        progressDialog.show();

        HttpUtils.sendHttpRequest(address, "username=" + username + "&password=" + password, new HttpUtils.Callback() {
            @Override
            public void onResponse(final HttpUtils.Response response) {
                checkResponseStatusCode(response.getStatusCode(), response);
                progressDialog.dismiss();
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                showMessage(e.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void checkResponseStatusCode(int statusCode, final HttpUtils.Response response) {
        switch (statusCode) {
            case 200:
                showMessage("欢迎来到逼乎社区", Toast.LENGTH_SHORT);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                    String username = mUsernameWrapper.getEditText().getText().toString();
                    String password = mPasswordWrapper.getEditText().getText().toString();
                    mEditor.putString("username", username);
                    mEditor.putString("password", password);
                    mEditor.putBoolean("isLogin", true);
                    mEditor.apply();
                }
                startNextActivity(response.string());
                break;
            case 400:
                mUsernameWrapper.setError(response.message());
                break;
            case 401:
                showMessage(response.message());
                break;
            case 500:
                showMessage(response.message());
                break;
        }
    }

    private void startNextActivity(String data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            mDialog.show();
        return super.onTouchEvent(event);
    }
}