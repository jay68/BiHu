package com.jay.bihu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jay.bihu.R;
import com.jay.bihu.bean.UserBean;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.JsonParser;
import com.jay.bihu.view.CircleImageView;

import java.io.IOException;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private RecyclerView mQuestionRv;

    private UserBean mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = JsonParser.getUser(getIntent().getStringExtra("data"));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mQuestionRv = (RecyclerView) findViewById(R.id.questionRv);

        setUpToolBar();
        setUpNavigationView();
        setUpQuestionRv();
    }

    private void setUpQuestionRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mQuestionRv.setLayoutManager(layoutManager);
    }

    private void setUpNavigationView() {
        mNavigationView.setCheckedItem(R.id.home);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.question:
                        break;
                    case R.id.avatar:
                        break;
                    case R.id.logout:
                        logout();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        View view = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        final CircleImageView avatar = (CircleImageView) view.findViewById(R.id.avatar);
        TextView username = (TextView) view.findViewById(R.id.username);

        username.setText(mUser.getUsername());
        if (mUser.getAvatar() != null) {
            HttpUtils.sendHttpRequest(mUser.getAvatar(), "", new HttpUtils.Callback() {
                @Override
                public void onResponse(HttpUtils.Response response) {
                    byte[] imgBytes = response.bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                    avatar.setImageBitmap(bitmap);
                }

                @Override
                public void onFail(IOException e) {
                    showMessage(e.getMessage());
                }
            });
        }
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLogin", false);
        editor.putString("username", "");
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
}
