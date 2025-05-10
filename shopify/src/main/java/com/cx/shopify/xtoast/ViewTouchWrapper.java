package com.cx.shopify.xtoast;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

@SuppressWarnings("rawtypes")
final class ViewTouchWrapper implements View.OnTouchListener {

    private final XToast<?> mToast;
    private final XToast.OnTouchListener mListener;

    ViewTouchWrapper(XToast<?> toast, XToast.OnTouchListener listener) {
        mToast = toast;
        mListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mListener == null) {
            return false;
        }
        return mListener.onTouch(mToast, view, event);
    }
}