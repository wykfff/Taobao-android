package com.example.taobao.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.taobao.base.BaseApplication;

public class keyboardUtil {
    public static boolean hide(View view) {
        InputMethodManager im = (InputMethodManager) BaseApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean show(View view) {
        InputMethodManager im = (InputMethodManager) BaseApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.showSoftInput(view, 0);
    }
}
