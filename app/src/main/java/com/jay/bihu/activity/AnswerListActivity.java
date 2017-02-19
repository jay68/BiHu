package com.jay.bihu.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jay.bihu.R;
import com.jay.bihu.adapter.AnswerListRvAdapter;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.Question;
import com.jay.bihu.data.User;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.JsonParser;

public class AnswerListActivity extends BaseActivity {
    private Question mQuestion;
    private User mUser;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mAnswerRv;
    private AnswerListRvAdapter mAnswerListRvAdapter;

    private boolean mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);

        Bundle data = getIntent().getBundleExtra("data");
        mUser = data.getParcelable("user");
        mQuestion = data.getParcelable("question");
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mAnswerRv = (RecyclerView) findViewById(R.id.answerRv);

        setUpToolBar();
        setUpAnswerRv();
        setUpRefreshLayout();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mLoading)
                    return;
                mLoading = true;
                HttpUtils.sendHttpRequest(ApiConfig.ANSWER_LIST, "qid=" + mQuestion.getId() + "&page=0&token=" + mUser.getToken(), new HttpUtils.Callback() {
                    @Override
                    public void onResponse(HttpUtils.Response response) {
                        mLoading = false;
                        mRefreshLayout.setRefreshing(false);
                        if (response.isSuccess())
                            mAnswerListRvAdapter.refreshAnswerList(JsonParser.getAnswerList(response.bodyString()));
                        else showMessage(response.message());
                    }

                    @Override
                    public void onFail(Exception e) {
                        mLoading = false;
                        showMessage(e.toString());
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void setUpAnswerRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAnswerRv.setLayoutManager(layoutManager);
        mAnswerListRvAdapter = new AnswerListRvAdapter(mUser, mQuestion);
        mAnswerRv.setAdapter(mAnswerListRvAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}
