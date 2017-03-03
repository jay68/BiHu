package com.jay.bihu.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jay.bihu.R;
import com.jay.bihu.adapter.AnswerListRvAdapter;
import com.jay.bihu.adapter.FavoriteListRvAdapter;
import com.jay.bihu.adapter.QuestionListRvAdapter;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.JsonParser;
import com.jay.bihu.utils.ToastUtils;

public class TailViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_ANSWER = 1;
    public static final int TYPE_FAVORITE = 2;

    private TextView mLoadTextView;
    private boolean loading;

    public TailViewHolder(View itemView) {
        super(itemView);
        mLoadTextView = (TextView) itemView.findViewById(R.id.load);
        loading = false;
    }

    public TextView getLoadTextView() {
        return mLoadTextView;
    }

    public void load(String address, String param, final RecyclerView.Adapter adapter, final int type) {
        int n = 1;  //Answer列表特殊处理
        if (type == TYPE_ANSWER)
            n = 2;
        if ((adapter.getItemCount() - n) % 10 != 0) {
            mLoadTextView.setText("没有更多了");
            return;
        }
        if (loading)
            return;
        mLoadTextView.setText("加载中...");
        loading = true;
        HttpUtils.sendHttpRequest(address, param, new HttpUtils.Callback() {
            @Override
            public void onResponse(HttpUtils.Response response) {
                loading = false;
                if (response.isSuccess()) {
                    String key = (type == TYPE_ANSWER ? "answers" : "questions");
                    String data = JsonParser.getElement(response.bodyString(), key);
                    if (data == null || data.equals("null") || data.equals("[]")) {
                        mLoadTextView.setText("没有更多了");
                        return;
                    }
                    if (type == TYPE_QUESTION)
                        ((QuestionListRvAdapter) adapter).addQuestion(JsonParser.getQuestionList(response.bodyString()));
                    else if (type == TYPE_ANSWER)
                        ((AnswerListRvAdapter) adapter).addAnswer(JsonParser.getAnswerList(response.bodyString()));
                    else if (type == TYPE_FAVORITE)
                        ((FavoriteListRvAdapter) adapter).addFavorite(JsonParser.getQuestionList(response.bodyString()));
                } else {
                    ToastUtils.showError(response.message());
                    mLoadTextView.setText("加载失败");
                }
            }

            @Override
            public void onFail(Exception e) {
                loading = false;
                ToastUtils.showError(e.toString());
                mLoadTextView.setText("加载失败");
            }
        });
    }
}
