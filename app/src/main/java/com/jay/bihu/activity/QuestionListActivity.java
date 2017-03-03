package com.jay.bihu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jay.bihu.R;
import com.jay.bihu.adapter.QuestionListRvAdapter;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.User;
import com.jay.bihu.utils.BitmapUtils;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.JsonParser;
import com.jay.bihu.utils.RefreshChecker;
import com.jay.bihu.utils.ToastUtils;
import com.jay.bihu.view.CircleImageView;
import com.jay.bihu.view.LoginDialog;

public class QuestionListActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private RecyclerView mQuestionRv;
    private SwipeRefreshLayout mRefreshLayout;
    private CircleImageView mAvatar;

    private User mUser;
    private QuestionListRvAdapter mQuestionListRvAdapter;

    private boolean mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        mUser = JsonParser.getUser(getIntent().getStringExtra("data"));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mQuestionRv = (RecyclerView) findViewById(R.id.questionRv);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        setUpToolBar();
        setUpNavigationView();
        setUpQuestionRv();
        setUpRefreshLayout();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (RefreshChecker.isQuestionNeedRefresh()) {
            uploadData();
            RefreshChecker.setQuestionNeedRefresh(false);
            mQuestionRv.scrollToPosition(0);
        }
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                uploadData();
            }
        });
    }

    private void uploadData() {
        if (mLoading)
            return;
        mLoading = true;
        HttpUtils.sendHttpRequest(ApiConfig.QUESTION_LIST, "page=0" + "&token=" + mUser.getToken(), new HttpUtils.Callback() {
            @Override
            public void onResponse(HttpUtils.Response response) {
                mLoading = false;
                mRefreshLayout.setRefreshing(false);
                if (response.isSuccess())
                    mQuestionListRvAdapter.refreshQuestionList(JsonParser.getQuestionList(response.bodyString()));
                else ToastUtils.showError(response.message());
            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.showError(e.toString());
                mRefreshLayout.setRefreshing(false);
                mLoading = false;
            }
        });
    }

    private void setUpQuestionRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mQuestionRv.setLayoutManager(layoutManager);
        mQuestionListRvAdapter = new QuestionListRvAdapter(mUser);
        mQuestionRv.setAdapter(mQuestionListRvAdapter);
    }

    private void setUpNavigationView() {
        //menu
        mNavigationView.setCheckedItem(R.id.home);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        mQuestionRv.scrollToPosition(0);
                        break;
                    case R.id.question:
                        actionStart(QuestionActivity.class);
                        break;
                    case R.id.favorite:
                        actionStart(FavoriteListActivity.class);
                        break;
                    case R.id.avatar:
                        checkAndOpenAlbum();
                        break;
                    case R.id.changePassword:
                        changePassword();
                        break;
                    case R.id.logout:
                        logout();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //header
        View view = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        TextView username = (TextView) view.findViewById(R.id.username);
        username.setText(mUser.getUsername());
        mAvatar = (CircleImageView) view.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndOpenAlbum();
            }
        });
        if (!mUser.getAvatarUrlString().equals("null"))
            HttpUtils.loadImage(mUser.getAvatarUrlString(), new HttpUtils.Callback() {
                @Override
                public void onResponse(HttpUtils.Response response) {
                    if (response.isSuccess())
                        mAvatar.setImageBitmap(BitmapUtils.toBitmap(response.bodyBytes()));
                    else ToastUtils.showError(response.message());
                }

                @Override
                public void onFail(Exception e) {
                    ToastUtils.showError(e.toString());
                }
            });
    }

    private void upLoadAvatar(Uri uri) {
        Bitmap avatar = BitmapUtils.toBitmap(uri);
        mAvatar.setImageBitmap(avatar);
        String name = System.currentTimeMillis() + "";
        String param = "token=" + mUser.getToken() + "&avatar=" + ApiConfig.QINIU_URL + name;
        HttpUtils.uploadImage(BitmapUtils.toBytes(avatar), name, param, ApiConfig.MODIFY_AVATAR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case OPEN_ALBUM:
                    cropImage(BitmapUtils.parseImageUriString(data), "file://" + getExternalCacheDir() + "/" + System.currentTimeMillis());
                    break;
                case CROP_IMAGE:
                    upLoadAvatar(data.getData());
                    break;
            }
    }

    private void changePassword() {
        final LoginDialog dialog = new LoginDialog(this);
        dialog.show();
        dialog.getMessageTextView().setText("更改密码");
        dialog.addPasswordWrapper("新" + getString(R.string.hint_password));
        dialog.setLoginButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = dialog.getPasswordWrapper().getEditText().getText().toString();
                if (password.length() < 5) {
                    dialog.getPasswordWrapper().setError("密码过短");
                    return;
                }
                String param = "token=" + mUser.getToken() + "&password=" + password;
                HttpUtils.sendHttpRequest(ApiConfig.CHANGE_PASSWORD, param, new HttpUtils.Callback() {
                    @Override
                    public void onResponse(HttpUtils.Response response) {
                        if (response.isSuccess()) {
                            dialog.dismiss();
                            ToastUtils.showHint(response.getInfo());
                            SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("password", password);
                            editor.apply();
                            mUser.setToken(JsonParser.getElement(response.bodyString(), "token"));
                        } else ToastUtils.showError(response.message());
                    }

                    @Override
                    public void onFail(Exception e) {
                        ToastUtils.showError(e.toString());
                    }
                });
            }
        });
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLogin", false);
        editor.putString("password", "");
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            mDrawerLayout.openDrawer(GravityCompat.START);
        return false;
    }

    private void actionStart(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("user", mUser);
        startActivity(intent);
    }
}
