package com.jay.bihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.bihu.R;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.Question;
import com.jay.bihu.data.User;
import com.jay.bihu.holder.QuestionViewHolder;
import com.jay.bihu.holder.TailViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavoriteListRvAdapter extends RecyclerView.Adapter {
    private static final int TYPE_QUESTION = 0;
    private static final int TYPE_TAIL = 1;

    private ArrayList<Question> mFavoriteList;
    private User mUser;

    public FavoriteListRvAdapter(User user, ArrayList<Question> favoriteList) {
        mUser = user;
        mFavoriteList = favoriteList;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_TAIL : TYPE_QUESTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_QUESTION:
                QuestionViewHolder holder = new QuestionViewHolder(inflater.inflate(R.layout.item_question, parent, false), mUser, mFavoriteList);
                holder.addOnClickListener();
                return holder;
            case TYPE_TAIL:
                final TailViewHolder tailViewHolder = new TailViewHolder(inflater.inflate(R.layout.item_tail, parent, false));
                tailViewHolder.getLoadTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String param = "page=" + mFavoriteList.size() / 20 + "&count=20&token=" + mUser.getToken();
                        tailViewHolder.load(ApiConfig.FAVORITE_LIST, param, FavoriteListRvAdapter.this, TailViewHolder.TYPE_FAVORITE);
                    }
                });
                return tailViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_QUESTION:
                QuestionViewHolder questionViewHolder = (QuestionViewHolder) holder;
                questionViewHolder.update(mFavoriteList.get(position));
                break;
            case TYPE_TAIL:
                String param = "page=" + mFavoriteList.size() / 20 + "&count=20&token=" + mUser.getToken();
                ((TailViewHolder) holder).load(ApiConfig.FAVORITE_LIST, param, this, TailViewHolder.TYPE_FAVORITE);
                break;
        }
    }

    public void refreshFavoriteList(ArrayList<Question> newQuestionList) {
        mFavoriteList.clear();
        addFavorite(newQuestionList);
    }

    public void addFavorite(ArrayList<Question> questionList) {
        mFavoriteList.addAll(questionList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size() + 1;
    }
}
