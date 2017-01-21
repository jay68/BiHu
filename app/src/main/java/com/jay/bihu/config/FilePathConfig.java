package com.jay.bihu.config;

import com.jay.bihu.utils.MyApplication;

/**
 * Created by Jay on 2017/1/20.
 * 关于一些命名规则（先写这吧）
 * 头像：avatar_uid
 * 图片：时间戳
 */

public class FilePathConfig {
    public static final String AVATAR_DIR = "file://" + MyApplication.getContext().getExternalCacheDir()+"/avatar/";    //使用时在其后拼接uid
    public static final String QINIU_URL = "ok4qp4ux0.bkt.clouddn.com/";   //使用时在其后拼接文件名
}
