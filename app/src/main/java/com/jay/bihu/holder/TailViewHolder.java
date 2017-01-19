package com.jay.bihu.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jay.bihu.R;
import com.jay.bihu.adapter.AnswerListRvAdapter;
import com.jay.bihu.adapter.FavoriteListRvAdapter;
import com.jay.bihu.adapter.QuestionListRvAdapter;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.utils.JsonParser;
import com.jay.bihu.utils.MyApplication;

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
        if (adapter.getItemCount() == 1 || (adapter.getItemCount() - 1) % 20 != 0) {
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
                    String questions = JsonParser.getElement(response.bodyString(), "questions");
                    if (questions == null || questions.equals("null")) {
                        mLoadTextView.setText("没有更多了");
                        return;
                    }
                    if (type == TYPE_QUESTION)
                        ((QuestionListRvAdapter) adapter).addQuestion(JsonParser.getQuestionList(response.bodyString()));
                    else if (type == TYPE_ANSWER)
                        ((AnswerListRvAdapter)adapter).addAnswer(JsonParser.getAnswerList(response.bodyString()));
                    else if (type == TYPE_FAVORITE)
                        ((FavoriteListRvAdapter)adapter).addFavorite(JsonParser.getQuestionList(response.bodyString()));

                } else {
                    Toast.makeText(MyApplication.getContext(), response.message(), Toast.LENGTH_LONG).show();
                    mLoadTextView.setText("加载失败");
                }
            }

            @Override
            public void onFail(Exception e) {
                loading = false;
                Toast.makeText(MyApplication.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                mLoadTextView.setText("加载失败");
            }
        });
    }
}
