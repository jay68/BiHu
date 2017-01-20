package com.jay.bihu.config;

import com.jay.bihu.utils.MyApplication;

/**
 * Created by Jay on 2017/1/20.
 */

public class FilePathConfig {
    public static final String AVATAR_DIR = "file://" + MyApplication.getContext().getExternalCacheDir()+"/avatar/";    //使用时在其后拼接uid
}
