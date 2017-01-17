package com.jay.bihu.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jay.bihu.R;
import com.jay.bihu.adapter.QuestionRvAdapter;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.User;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.JsonParser;

/**
 * Created by Jay on 2017/1/17.
 */

public class FavoriteActivity extends BaseActivity {
    private String mToken;

    private RecyclerView mQuestionRv;
    private SwipeRefreshLayout mRefreshLayout;
    private QuestionRvAdapter mQuestionRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mToken = getIntent().getBundleExtra("data").getString("token");
        mQuestionRv = (RecyclerView) findViewById(R.id.questionRv);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        setUpQuestionRv();
        setUpRefreshLayout();
    }

    private void setUpQuestionRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mQuestionRv.setLayoutManager(layoutManager);

        HttpUtils.sendHttpRequest(ApiConfig.FAVORITE_LIST, "token=" + mToken + "&page=0&count=20", new HttpUtils.Callback() {
            @Override
            public void onResponse(HttpUtils.Response response) {
                if (response.isSuccess()) {
                    User user = new User();
                    user.setToken(mToken);
                    mQuestionRvAdapter = new QuestionRvAdapter(user, JsonParser.getFavoriteList(response.bodyString()));
                    mQuestionRv.setAdapter(mQuestionRvAdapter);
                } else showMessage(response.message());
            }

            @Override
            public void onFail(Exception e) {
                showMessage(e.toString());
            }
        });
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpUtils.sendHttpRequest(ApiConfig.FAVORITE_LIST, "token=" + mToken + "&page=0&count=20", new HttpUtils.Callback() {
                    @Override
                    public void onResponse(HttpUtils.Response response) {
                        mRefreshLayout.setRefreshing(false);
                        if (response.isSuccess())
                            mQuestionRvAdapter.refreshQuestionList(JsonParser.getFavoriteList(response.bodyString()));
                        else showMessage(response.message());
                    }

                    @Override
                    public void onFail(Exception e) {
                        showMessage(e.toString());
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
}
