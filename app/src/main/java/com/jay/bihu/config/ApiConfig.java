package com.jay.bihu.config;

/**
 * Created by Jay on 2017/1/12.
 * 逼乎的常量表
 */

public class ApiConfig {
    public static final String REGISTER = "https://api.caoyue.com.cn/bihu/register.php";
    public static final String LOGIN = "https://api.caoyue.com.cn/bihu/login.php";
    public static final String MODIFY_AVATAR = "https://api.caoyue.com.cn/bihu/modifyAvatar.php";   //修改头像
    public static final String QUESTION_LIST = "https://api.caoyue.com.cn/bihu/getQuestionList.php";
    public static final String ANSWER_LIST = "https://api.caoyue.com.cn/bihu/getAnswerList.php";
    public static final String POST_QUESTION = "https://api.caoyue.com.cn/bihu/question.php";
    public static final String POST_ANSWER = "https://api.caoyue.com.cn/bihu/answer.php";
    public static final String ACCEPT = "https://api.caoyue.com.cn/bihu/accept.php";    //采纳
    public static final String EXCITING = "https://api.caoyue.com.cn/bihu/exciting.php";    //赞
    public static final String NAIVE = "https://api.caoyue.com.cn/bihu/naive.php";  //嘲讽
    public static final String CANCEL_EXCITING = "https://api.caoyue.com.cn/bihu/cancelExciting.php";
    public static final String CANCEL_NAIVE = "https://api.caoyue.com.cn/bihu/cancelNaive.php";
    public static final String UPLOAD_IMAGE = "https://api.caoyue.com.cn/bihu/newImage.php";
    public static final String GET_IMAGE = "https://api.caoyue.com.cn/bihu/getImage.php";
    public static final String FAVORITE_LIST = "https://api.caoyue.com.cn/bihu/getFavoriteList.php";    //收藏
    public static final String CANCEL_FAVORITE = "https://api.caoyue.com.cn/bihu/cancelFavorite.php";
    public static final String FAVORITE = "https://api.caoyue.com.cn/bihu/favorite.php";
    public static final String CHANGE_PASSWORD = "https://api.caoyue.com.cn/bihu/changePassword.php";
    public static final String GET_TOKEN = "http://api.jay-li.cn/qiniu/getToken.php";
    public static final String QINIU_URL = "http://ok4qp4ux0.bkt.clouddn.com/";   //使用时在其后拼接文件名
}
