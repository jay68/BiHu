package com.jay.bihu.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jay.bihu.R;
import com.jay.bihu.config.ApiConfig;
import com.jay.bihu.data.Answer;
import com.jay.bihu.data.Question;
import com.jay.bihu.data.User;
import com.jay.bihu.utils.BitmapUtils;
import com.jay.bihu.utils.DateUtils;
import com.jay.bihu.utils.HttpUtils;
import com.jay.bihu.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/1/19.
 */

public class AnswerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CircleImageView mAvatar;

    private TextView mAuthorName;
    private TextView mDate;
    private TextView mAnswerContent;
    private TextView mExcitingCount;
    private TextView mNaiveCount;

    private ImageButton mExcitingButton;
    private ImageButton mNaiveButton;
    private ImageButton mAcceptButton;

    private ArrayList<Answer> mAnswerList;
    private Question mQuestion;
    private User mUser;

    public AnswerViewHolder(View itemView, User user, ArrayList<Answer> answerList) {
        super(itemView);

        mAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        mAuthorName = (TextView) itemView.findViewById(R.id.authorName);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mAnswerContent = (TextView) itemView.findViewById(R.id.answerContent);
        mExcitingCount = (TextView) itemView.findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) itemView.findViewById(R.id.naiveCount);
        mNaiveButton = (ImageButton) itemView.findViewById(R.id.naiveButton);
        mExcitingButton = (ImageButton) itemView.findViewById(R.id.excitingButton);
        mAcceptButton = (ImageButton) itemView.findViewById(R.id.acceptButton);

        mUser = user;
        mAnswerList = answerList;
    }

    public void setAcceptButtonVisible(boolean visible) {
        mAcceptButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void update(Answer answer, Question question) {
        mQuestion = question;
        updateAllImage(answer);
        updateAllTextView(answer);
    }

    private void updateAllTextView(Answer answer) {
        mAuthorName.setText(answer.getAuthorName());
        mDate.setText(DateUtils.getDateDescription(answer.getDate()));
        mAnswerContent.setText(answer.getContent());
        mNaiveCount.setText("(" + answer.getNaiveCount() + ")");
        mExcitingCount.setText("(" + answer.getExcitingCount() + ")");
    }

    private void updateAllImage(Answer answer) {
        mNaiveButton.setBackgroundResource(answer.isNaive() ? R.drawable.ic_thumb_down_pink_24dp : R.drawable.ic_thumb_down_gray_24dp);
        mExcitingButton.setBackgroundResource(answer.isExciting() ? R.drawable.ic_thumb_up_pink_24dp : R.drawable.ic_thumb_up_gray_24dp);
        mAcceptButton.setBackgroundResource(answer.isBest() ? R.drawable.ic_accept_pink_24dp : R.drawable.ic_accept_gray_24dp);

        if (answer.getAuthorAvatarUrlString().equals("null"))
            mAvatar.setImageResource(R.mipmap.default_avatar);
        else
            HttpUtils.loadImage(answer.getAuthorAvatarUrlString(), new HttpUtils.Callback() {
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

    public void addOnclickListeners() {
        mExcitingButton.setOnClickListener(this);
        mNaiveButton.setOnClickListener(this);
        mAcceptButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Answer answer = mAnswerList.get(getLayoutPosition() - 1);
        String param = "id=" + answer.getId() + "&type=2&token=" + mUser.getToken();
        switch (v.getId()) {
            case R.id.naiveButton:
                if (answer.isNaive()) {
                    HttpUtils.sendHttpRequest(ApiConfig.CANCEL_NAIVE, param);
                    mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_gray_24dp);
                    answer.setNaiveCount(answer.getNaiveCount() - 1);
                    answer.setNaive(false);
                } else {
                    HttpUtils.sendHttpRequest(ApiConfig.NAIVE, param);
                    mNaiveButton.setBackgroundResource(R.drawable.ic_thumb_down_pink_24dp);
                    answer.setNaiveCount(answer.getNaiveCount() + 1);
                    answer.setNaive(true);
                }
                mNaiveCount.setText("(" + answer.getNaiveCount() + ")");
                break;
            case R.id.excitingButton:
                if (answer.isExciting()) {
                    HttpUtils.sendHttpRequest(ApiConfig.CANCEL_EXCITING, param);
                    mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_gray_24dp);
                    answer.setExcitingCount(answer.getExcitingCount() - 1);
                    answer.setExciting(false);
                } else {
                    HttpUtils.sendHttpRequest(ApiConfig.EXCITING, param);
                    mExcitingButton.setBackgroundResource(R.drawable.ic_thumb_up_pink_24dp);
                    answer.setExcitingCount(answer.getExcitingCount() + 1);
                    answer.setExciting(true);
                }
                mExcitingCount.setText("(" + answer.getExcitingCount() + ")");
                break;
            case R.id.acceptButton:
                param = "qid=" + mQuestion.getId() + "&aid=" + answer.getId() + "&token=" + mUser.getToken();
                HttpUtils.sendHttpRequest(ApiConfig.ACCEPT, param);
                mAcceptButton.setBackgroundResource(R.drawable.ic_accept_pink_24dp);
                answer.setBest(true);
                break;
        }
    }
}
