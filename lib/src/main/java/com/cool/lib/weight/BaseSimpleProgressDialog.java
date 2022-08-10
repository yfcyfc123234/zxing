package com.cool.lib.weight;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.cool.lib.R;

public class BaseSimpleProgressDialog extends ProgressDialog {
    private final boolean cancelable;

    public BaseSimpleProgressDialog(Context context) {
        this(context, false);
    }

    public BaseSimpleProgressDialog(Context context, boolean cancelable) {
        super(context, R.style.QuProgressDialog);
        this.cancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);

        setContentView(R.layout.dialog_progress);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
}
