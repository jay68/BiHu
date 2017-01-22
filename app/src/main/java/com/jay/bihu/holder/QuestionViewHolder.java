package com.jay.bihu.holder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jay.bihu.R;
import com.jay.bihu.activity.AnswerActivity;
import com.jay.bihu.activity.AnswerListActivity;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.Question;
import com.jay.bihu.data.User;
import com.jay.bihu.utils.BitmapUtils;
import com.jay.bihu.utils.DateUtils;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.view.CircleImageView;

import java.util.ArrayList;

public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CircleImageView mAvatar;
    private ImageView mQuestionImage;

    private TextView mAuthorName;
    private TextView mDate;
    private TextView mRecentDate;
    private TextView mQuestionTitle;
    private TextView mQuestionDetail;
    private TextView mExcitingCount;
    private TextView mNaiveCount;
    private TextView mAnswerCount;

    private ImageButton mExcitingButton;
    private ImageButton mNaiveButton;
    private ImageButton mAnswerButton;
    private ImageButton mFavoriteButton;

    private User mUser;
    private ArrayList<Question> mQuestionList;

    public QuestionViewHolder(View itemView, User user, ArrayList<Question> questionList) {
        super(itemView);
        setItemViewOnClickListener(itemView);

        mAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        mQuestionImage = (ImageView) itemView.findViewById(R.id.questionImage);
        mAuthorName = (TextView) itemView.findViewById(R.id.authorName);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mRecentDate = (TextView) itemView.findViewById(R.id.recentDate);
        mQuestionTitle = (TextView) itemView.findViewById(R.id.questionTitle);
        mQuestionDetail = (TextView) itemView.findViewById(R.id.questionDetail);
        mExcitingCount = (TextView) itemView.findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) itemView.findViewById(R.id.naiveCount);
        mAnswerCount = (TextView) itemView.findViewById(R.id.answerCount);
        mNaiveButton = (ImageButton) itemView.findViewById(R.id.naiveButton);
        mExcitingButton = (ImageButton) itemView.findViewById(R.id.excitingButton);
        mAnswerButton = (ImageButton) itemView.findViewById(R.id.answerButton);
        mFavoriteButton = (ImageButton) itemView.findViewById(R.id.favoriteButton);

        mUser = user;
        mQuestionList = questionList;
    }

    private void setItemViewOnClickListener(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AnswerListActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("user", mUser);
                data.putParcelable("question", mQuestionList.get(getLayoutPosition()));
                intent.putExtra("data", data);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void update(Question question) {
        updateAllImage(question);
        updateAllTextView(question);
    }

    private void updateAllTextView(Question question) {
        mAuthorName.setText(question.getAuthorName());
        mDate.setText(DateUtils.getDateDescription(question.getDate()));
        mRecentDate.setText(DateUtils.getDateDescription(question.getRecent()) + " 更新");
        mQuestionTitle.setText(question.getTitle());
        mQuestionDetail.setText(question.getContent());
        mExcitingCount.setText("(" + question.getExcitingCount() + ")");
        mNaiveCount.setText("(" + question.getNaiveCount() + ")");
        mAnswerCount.setText("(" + question.getAnswerCount() + ")");
    }

    private void updateAllImage(final Question question) {
        mNaiveButton.setBackgroundResource(question.isNaive() ? R.drawable.ic_thumb_down_pink_24dp : R.drawable.ic_thumb_down_gray_24dp);
        mExcitingButton.setBackgroundResource(question.isExciting() ? R.drawable.ic_thumb_up_pink_24dp : R.drawable.ic_thumb_up_gray_24dp);
        mFavoriteButton.setBackgroundResource(question.isFavorite() ? R.drawable.ic_favorite_pink_24dp : R.drawable.ic_favorite_border_gray_24dp);

        if (question.getAuthorAvatarUrlString().equals("null"))
            mAvatar.setImageResource(R.mipmap.default_avatar);
        else
            HttpUtils.loadImage(question.getAuthorAvatarUrlString(), new HttpUtils.Callback() {
                @Override
                public void onResponse(HttpUtils.Response response) {
                    if (response.isSuccess())
                        mAvatar.setImageBitmap(BitmapUtils.toBitmap(response.bodyBytes()));
                }

                @Override
                public void onFail(Exception e) {

                }
            });
    }

    public void addOnClickListener() {
        mExcitingButton.setOnClickListener(this);
        mNaiveButton.setOnClickListener(this);
        mFavoriteButton.setOnClickListener(this);
        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AnswerActivity.class);
                intent.putExtra("qid", mQuestionList.get(getLayoutPosition()).getId());
                intent.putExtra("token", mUser.getToken());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Question question = mQuestionList.get(getLayoutPosition());
        String param = "id=" + question.getId() + "&type=1&token=" + mUser.getToken();
        switch (v.getId()) {
            case R.id.naiveButton:
                if (question.isNaive()) {
                    HttpUtils.sendHttpRequest(ApiConfig.CANCEL_NAIVE, param);
                    mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_gray_24dp);
                    question.setNaiveCount(question.getNaiveCount() - 1);
                    question.setNaive(false);
                } else {
                    HttpUtils.sendHttpRequest(ApiConfig.NAIVE, param);
                    mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_pink_24dp);
                    question.setNaiveCount(question.getNaiveCount() + 1);
                    question.setNaive(true);
                }
                mNaiveCount.setText("(" + question.getNaiveCount() + ")");
                break;
            case R.id.excitingButton:
                if (question.isExciting()) {
                    HttpUtils.sendHttpRequest(ApiConfig.CANCEL_EXCITING, param);
                    mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_gray_24dp);
                    question.setExcitingCount(question.getExcitingCount() - 1);
                    question.setExciting(false);
                } else {
                    HttpUtils.sendHttpRequest(ApiConfig.EXCITING, param);
                    mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_pink_24dp);
                    question.setExcitingCount(question.getExcitingCount() + 1);
                    question.setExciting(true);
                }
                mExcitingCount.setText("(" + question.getExcitingCount() + ")");
                break;
            case R.id.favoriteButton:
                param = "qid=" + question.getId() + "&token=" + mUser.getToken();
                if (question.isFavorite()) {
                    HttpUtils.sendHttpRequest(ApiConfig.CANCEL_FAVORITE, param);
                    question.setFavorite(false);
                    mFavoriteButton.setBackgroundResource(R.drawable.ic_favorite_border_gray_24dp);
                } else {
                    HttpUtils.sendHttpRequest(ApiConfig.FAVORITE, param);
                    question.setFavorite(true);
                    mFavoriteButton.setBackgroundResource(R.drawable.ic_favorite_pink_24dp);
                }
                break;
        }
    }
}
