package com.onairm.baselibrary.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanzhikai.pictureprogressbar.PictureProgressBar;

/**
 * Created by android on 2017/10/12.
 */

public class DownProgressDialog extends Dialog {

    protected PictureProgressBar progressBar;
    protected TextView tvTitle;

    public DownProgressDialog(@NonNull Context context) {
        super(context);
    }

    public DownProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public PictureProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }
}
