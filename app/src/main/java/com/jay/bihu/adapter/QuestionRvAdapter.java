package com.jay.bihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.bihu.R;
import com.jay.bihu.data.Question;
import com.jay.bihu.holder.QuestionListViewHolder;
import com.jay.bihu.holder.TailViewHolder;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/1/14.
 */

public class QuestionRvAdapter extends RecyclerView.Adapter {
    private static final int TYPE_QUESTION = 0;
    private static final int TYPE_TAIL = 1;

    private ArrayList<Question> mQuestionList;

    public QuestionRvAdapter(ArrayList<Question> questionList) {
        mQuestionList = questionList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return TYPE_TAIL;
        return TYPE_QUESTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_QUESTION:
                QuestionListViewHolder holder = new QuestionListViewHolder(inflater.inflate(R.layout.item_question, parent, false));
                holder.addOnClickListener();
                return holder;
            case TYPE_TAIL:
                final TailViewHolder tailViewHolder = new TailViewHolder(inflater.inflate(R.layout.item_tail, parent, false));
                tailViewHolder.getLoad().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tailViewHolder.loading(QuestionRvAdapter.this, mQuestionList.size() / 20);
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
                QuestionListViewHolder questionListViewHolder = (QuestionListViewHolder) holder;
                questionListViewHolder.updateAllTextView(mQuestionList.get(position));
                questionListViewHolder.updateAllImageView(mQuestionList.get(position));
                break;
            case TYPE_TAIL:
                ((TailViewHolder) holder).loading(this, mQuestionList.size() / 20);
                break;
        }
    }

    public void refreshQuestionList(ArrayList<Question> newQuestionList) {
        mQuestionList = newQuestionList;
        notifyDataSetChanged();
    }

    public void addQuestion(ArrayList<Question> questionList) {
        mQuestionList.addAll(questionList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size() + 1;
    }
}
