package com.jay.bihu.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jay.bihu.R;
import com.jay.bihu.adapter.QuestionRvAdapter;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.JsonParser;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Jay on 2017/1/14.
 */

public class TailViewHolder extends RecyclerView.ViewHolder {
    private TextView mLoad;
    private boolean loading;

    public TailViewHolder(View itemView) {
        super(itemView);
        mLoad = (TextView) itemView.findViewById(R.id.load);
        loading = false;
    }

    public TextView getLoad() {
        return mLoad;
    }

    public void loading(final QuestionRvAdapter adapter, int page) {
        if ((adapter.getItemCount() - 1) % 20 != 0) {
            loadedAll();
            return;
        }

        mLoad.setText("加载中...");
        loading = true;
        HttpUtils.sendHttpRequest(ApiConfig.QUESTION_LIST, "page=" + page + "&count=20", new HttpUtils.Callback() {
            @Override
            public void onResponse(HttpUtils.Response response) {
                loading = false;
                if (response.isSuccess()) {
                    String questions = JsonParser.getElement(response.string(), "questions");
                    if (questions == null || questions.equals("null")) {
                        loadedAll();
                        return;
                    }
                    adapter.addQuestion(JsonParser.getQuestionList(response.string()));
                }
            }

            @Override
            public void onFail(IOException e) {
                loading = false;
                loadFailed();
            }
        });
    }

    public void loadFailed() {
        mLoad.setText("加载失败");
    }

    public void loadedAll() {
        mLoad.setText("没有更多了");
    }
}
