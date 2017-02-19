package com.jay.bihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.bihu.R;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.Answer;
import com.jay.bihu.data.Question;
import com.jay.bihu.data.User;
import com.jay.bihu.holder.AnswerViewHolder;
import com.jay.bihu.holder.QuestionViewHolder;
import com.jay.bihu.holder.TailViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AnswerListRvAdapter extends RecyclerView.Adapter {
    private static final int TYPE_QUESTION = 0;
    private static final int TYPE_ANSWER = 1;
    private static final int TYPE_TAIL = 2;

    private Question mQuestion;
    private ArrayList<Answer> mAnswerList;
    private User mUser;

    public AnswerListRvAdapter(User user, Question question) {
        mUser = user;
        mAnswerList = new ArrayList<>();
        mQuestion = question;
        sort();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_QUESTION;
        else if (position == getItemCount() - 1)
            return TYPE_TAIL;
        else return TYPE_ANSWER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_QUESTION:
                ArrayList<Question> questionList = new ArrayList<>();
                questionList.add(mQuestion);
                QuestionViewHolder questionViewHolder = new QuestionViewHolder(inflater.inflate(R.layout.item_question, parent, false), mUser, questionList);
                questionViewHolder.addOnClickListener();
                return questionViewHolder;
            case TYPE_ANSWER:
                AnswerViewHolder answerViewHolder = new AnswerViewHolder(inflater.inflate(R.layout.item_answer, parent, false), mUser, mAnswerList);
                answerViewHolder.addOnclickListeners();
                if (mQuestion.getAuthorId() == mUser.getId())
                    answerViewHolder.setAcceptButtonVisible(true);
                return answerViewHolder;
            case TYPE_TAIL:
                final TailViewHolder tailViewHolder = new TailViewHolder(inflater.inflate(R.layout.item_tail, parent, false));
                tailViewHolder.getLoadTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String param = "page=" + mAnswerList.size() / 10 + "&qid=" + mQuestion.getId() + "&token=" + mUser.getToken();
                        tailViewHolder.load(ApiConfig.ANSWER_LIST, param, AnswerListRvAdapter.this, TailViewHolder.TYPE_ANSWER);
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
                questionViewHolder.update(mQuestion);
                break;
            case TYPE_ANSWER:
                AnswerViewHolder answerViewHolder = (AnswerViewHolder) holder;
                answerViewHolder.update(mAnswerList.get(position - 1), mQuestion);
                break;
            case TYPE_TAIL:
                String param = "page=" + mAnswerList.size() / 10 + "&qid=" + mQuestion.getId() + "&token=" + mUser.getToken();
                ((TailViewHolder) holder).load(ApiConfig.ANSWER_LIST, param, this, TailViewHolder.TYPE_ANSWER);
                break;
        }
    }

    public void refreshAnswerList(ArrayList<Answer> newAnswerList) {
        mAnswerList.clear();
        addAnswer(newAnswerList);
    }

    public void addAnswer(ArrayList<Answer> answerList) {
        mAnswerList.addAll(answerList);
        sort();
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mAnswerList.size() + 2;
    }

    private void sort() {
        Collections.sort(mAnswerList, new Comparator<Answer>() {
            @Override
            public int compare(Answer o1, Answer o2) {
                if (o1.isBest())
                    return -1;
                else if (o2.isBest())
                    return 1;
                else return 0;
            }
        });
    }
}
