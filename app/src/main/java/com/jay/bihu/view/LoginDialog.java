package com.jay.bihu.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.jay.bihu.R;

/**
 * Created by Jay on 2017/1/13.
 */

public class LoginDialog extends Dialog {
    private Context mContext;

    public LoginDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);
    }
}
