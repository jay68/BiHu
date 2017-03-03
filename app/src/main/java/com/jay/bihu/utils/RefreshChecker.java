package com.jay.bihu.utils;

/**
 * Created by Jay on 2017/2/26.
 * 判断问题或回答列表是否应该被更新（前面的代码写废了，只有这样搞了）
 */

public class RefreshChecker {
    private static boolean sQuestionNeedRefresh = false;
    private static boolean sAnswerNeedRefresh = false;

    public static boolean isQuestionNeedRefresh() {
        return sQuestionNeedRefresh;
    }

    public static void setQuestionNeedRefresh(boolean questionNeedRefresh) {
        sQuestionNeedRefresh = questionNeedRefresh;
    }

    public static boolean isAnswerNeedRefresh() {
        return sAnswerNeedRefresh;
    }

    public static void setAnswerNeedRefresh(boolean answerNeedRefresh) {
        sAnswerNeedRefresh = answerNeedRefresh;
    }
}
