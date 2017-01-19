package com.jay.bihu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jay.bihu.R;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.utils.HttpUtils;

public class AnswerActivity extends BaseActivity {
    private String mToken;
    private int mQid;
    private boolean isCommitting;

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mQid = intent.getIntExtra("qid", -1);

        mEditText = (EditText) findViewById(R.id.answerContent);
        setUpToolBar();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isCommitting)
            return true;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.commit:
                commit();
                break;
            case R.id.add_image:
//                addImage();
                break;
        }
        return true;
    }

    private void commit() {
        if (isCommitting)
            return;
        String content = mEditText.getText().toString();
        if (content.equals("")) {
            showMessage("内容不能为空");
            return;
        }
        isCommitting = true;
        String param = "qid=" + mQid + "&content=" + content + "&token=" + mToken;
        HttpUtils.sendHttpRequest(ApiConfig.POST_ANSWER, param, new HttpUtils.Callback() {
            @Override
            public void onResponse(HttpUtils.Response response) {
                if (response.isSuccess()) {
                    showMessage("已提交", Toast.LENGTH_SHORT);
                    finish();
                }
                else showMessage(response.message());
                isCommitting = false;
            }

            @Override
            public void onFail(Exception e) {
                isCommitting = false;
                showMessage(e.toString());
            }
        });
    }
}
