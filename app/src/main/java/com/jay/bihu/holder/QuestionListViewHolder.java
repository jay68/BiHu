package com.jay.bihu.holder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jay.bihu.R;
import com.jay.bihu.data.Question;
import com.jay.bihu.utils.BitmapUtils;
import com.jay.bihu.utils.DateUtils;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.view.CircleImageView;

import java.io.IOException;

/**
 * Created by Jay on 2017/1/14.
 */

public class QuestionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CircleImageView mAvatar;
    private ImageView mQuestionImage;

    private TextView mAuthorName;
    private TextView mDate;
    private TextView mQuestionTitle;
    private TextView mQuestionDetail;
    private TextView mExcitingCount;
    private TextView mNaiveCount;
    private TextView mAnswerCount;

    private ImageButton mExcitingButton;
    private ImageButton mNaiveButton;
    private ImageButton mAnswerButton;
    private ImageButton mFavoriteButton;

    public QuestionListViewHolder(View itemView) {
        super(itemView);
        mAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        mQuestionImage = (ImageView) itemView.findViewById(R.id.questionImage);
        mAuthorName = (TextView) itemView.findViewById(R.id.authorName);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mQuestionTitle = (TextView) itemView.findViewById(R.id.questionTitle);
        mQuestionDetail = (TextView) itemView.findViewById(R.id.questionDetail);
        mExcitingCount = (TextView) itemView.findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) itemView.findViewById(R.id.naiveCount);
        mAnswerCount = (TextView) itemView.findViewById(R.id.answerCount);
        mNaiveButton = (ImageButton) itemView.findViewById(R.id.naiveButton);
        mExcitingButton = (ImageButton) itemView.findViewById(R.id.excitingButton);
        mAnswerButton = (ImageButton) itemView.findViewById(R.id.answerButton);
        mFavoriteButton = (ImageButton) itemView.findViewById(R.id.favoriteButton);
    }

    public void updateAllTextView(Question question) {
        mAuthorName.setText(question.getAuthorName());
        mDate.setText(DateUtils.getDateDescription(question.getDate()));
        mQuestionTitle.setText(question.getTitle());
        mQuestionDetail.setText(question.getContent());
        mExcitingCount.setText("(" + question.getExciting() + ")");
        mNaiveCount.setText("(" + question.getNaive() + ")");
        mAnswerCount.setText("(" + question.getAnswerCount() + ")");
    }

    public void updateAllImageView(final Question question) {
        //获取图片
    }

    public void addOnClickListener() {
        mExcitingButton.setOnClickListener(this);
        mNaiveButton.setOnClickListener(this);
        mAnswerButton.setOnClickListener(this);
        mFavoriteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.naiveButton:
                break;
            case R.id.excitingButton:
                break;
            case R.id.answerButton:
                break;
            case R.id.favoriteButton:
                break;
        }
    }
}
