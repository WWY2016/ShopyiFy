package com.cx.shopify.xtoast;

import android.view.View;


@SuppressWarnings("rawtypes")
final class ViewLongClickWrapper implements View.OnLongClickListener {

    private final  XToast<?> mToast;
    private final XToast.OnLongClickListener mListener;

    ViewLongClickWrapper(XToast<?> toast, XToast.OnLongClickListener listener) {
        mToast = toast;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean onLongClick(View view) {
        if (mListener == null) {
            return false;
        }
        return mListener.onLongClick(mToast, view);
    }
}