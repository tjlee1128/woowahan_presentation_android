package com.baemin.woowahan_presentation_android.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.baemin.woowahan_presentation_android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class WPMessageDialog extends Dialog {

    @Bind(R.id.dialog_messagingbox_tv_title)
    TextView titleTextView;
    @Bind(R.id.dialog_messagingbox_content_tv)
    TextView messageTextView;
    private String title;
    private String message;

    @Bind(R.id.dialog_messagingbox_btn_save)
    Button saveButton;

    private View.OnClickListener mSaveListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_messagingbox);

        ButterKnife.bind(this);

        init();
    }

    public WPMessageDialog(Context context, String title, String message, View.OnClickListener onClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.message = message;
        this.mSaveListener = onClickListener;
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setMessage(String message) {
        messageTextView.setText(message);
    }

    private void setOnClickListener(View.OnClickListener onClickListener) {
        saveButton.setOnClickListener(onClickListener);
    }

    private void init() {
        setTitle(title);
        setMessage(message);
        setOnClickListener(mSaveListener);
    }
}
