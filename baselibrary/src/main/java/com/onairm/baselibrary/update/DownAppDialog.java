package com.onairm.baselibrary.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onairm.tv.baselibrary.R;

/**
 * Created by android on 2017/3/1.
 */

public class DownAppDialog extends Dialog {

    protected Context context;

    protected String title;
    protected String describe;
    protected String message;

    protected PositiveClickListener positiveClickListener;
    protected NegativeClickListener negativeClickListener;
    protected NegativeGoneListener negativeGoneListener;

    public DownAppDialog(@NonNull Context context) {
        super(context);
    }

    public DownAppDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(int message) {
        this.message = (String) context.getText(message);
    }

    public void setPositiveClickListener(PositiveClickListener positiveClickListener) {
        this.positiveClickListener = positiveClickListener;
    }

    public void setNegativeClickListener(NegativeClickListener negativeClickListener) {
        this.negativeClickListener = negativeClickListener;
    }

    public void setNegativeGoneListener(NegativeGoneListener negativeGoneListener) {
        this.negativeGoneListener = negativeGoneListener;
    }

    public interface NegativeGoneListener {
        boolean negativeGone();
    }

    public interface PositiveClickListener {
        void positiveClick(TextView view);
    }

    public interface NegativeClickListener {
        void negativeClick(TextView view);
    }
}
