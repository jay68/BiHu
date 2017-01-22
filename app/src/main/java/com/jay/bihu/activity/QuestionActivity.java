package com.jay.bihu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jay.bihu.R;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.User;
import com.jay.bihu.utils.BitmapUtils;
import com.jay.bihu.utils.HttpUtils;

import java.io.File;

public class QuestionActivity extends BaseActivity {
    private User mUser;
    private boolean isCommitting;

    private EditText mQuestionTitle;
    private EditText mQuestionDetail;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mUser = getIntent().getBundleExtra("data").getParcelable("user");
        mQuestionDetail = (EditText) findViewById(R.id.questionDetail);
        mQuestionTitle = (EditText) findViewById(R.id.questionTitle);

        setUpToolBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case OPEN_ALBUM:
                    File file = new File(getExternalCacheDir(), "img");
                    if (!file.exists())
                        file.mkdir();
                    cropImage(BitmapUtils.parseImageUriString(data), "file://" + file.getPath() + "/" + System.currentTimeMillis());
                    break;
                case CROP_IMAGE:
                    mImageUri = data.getData();
                    break;
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                break;
        }
        return true;
    }

    private void commit() {
        if (isCommitting)
            return;
        String title = mQuestionTitle.getText().toString();
        String content = mQuestionDetail.getText().toString();
        if (title.equals("")) {
            showMessage("标题不能为空");
            return;
        } else if (content.equals("")) {
            showMessage("内容不能为空");
            return;
        }
        String param = "title=" + title + "&content=" + content + "&token=" + mUser.getToken();
        isCommitting = true;
        HttpUtils.sendHttpRequest(ApiConfig.POST_QUESTION, param, new HttpUtils.Callback() {
            @Override
            public void onResponse(HttpUtils.Response response) {
                isCommitting = false;
                if (response.isSuccess()) {
                    showMessage("已提交", Toast.LENGTH_SHORT);
                    finish();
                } else showMessage(response.message());
            }

            @Override
            public void onFail(Exception e) {
                isCommitting = false;
                showMessage(e.toString());
            }
        });
    }
}
