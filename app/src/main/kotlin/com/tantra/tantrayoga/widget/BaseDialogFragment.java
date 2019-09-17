package com.tantra.tantrayoga.widget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.tantra.tantrayoga.R;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public abstract class BaseDialogFragment<T> extends AppCompatDialogFragment {

    @Retention(SOURCE)
    @IntDef({DialogInterface.BUTTON_POSITIVE, DialogInterface.BUTTON_NEGATIVE, DialogInterface.BUTTON_NEUTRAL})
    public @interface ButtonType {}

    protected static final String TITLE_ARG = "title";
    protected static final String TITLE_RES_ARG = "titleRes";

    protected CharSequence title;
    protected Callback<T> callbackListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        Bundle args = getArguments();
        int titleRes = args.getInt(TITLE_RES_ARG);
        if (titleRes != 0) {
            title = getString(titleRes);
        } else {
            title = args.getCharSequence(TITLE_ARG);
        }
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(title)) {
            TextView titleView = (TextView) view.findViewById(R.id.mcm_dialog_title);
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);
        }
    }

    public interface Callback<T> {
        void onButtonClick(T dialogState, @ButtonType int button);

        void onCancel(T dialogState);
    }

    public void setCallbackListener(Callback<T> callbackListener) {
        this.callbackListener = callbackListener;
    }

    protected void dispatchOnButtonClick(T content, @ButtonType int witch) {
        if (callbackListener != null) {
            callbackListener.onButtonClick(content, witch);
        }
    }

    protected void dispatchOnCancel(T content) {
        if (callbackListener != null) {
            callbackListener.onCancel(content);
        }
    }

    protected static class DefaultOnButtonClickListener<D extends BaseDialogFragment<S>, S> implements View.OnClickListener {

        private final D dialog;
        private final S state;

        public DefaultOnButtonClickListener(D dialog, S state) {
            this.dialog = dialog;
            this.state = state;
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.dialog_ok) {
                dialog.dispatchOnButtonClick(state, DialogInterface.BUTTON_POSITIVE);
            } else if (viewId == R.id.dialog_cancel) {
                dialog.dispatchOnButtonClick(state, DialogInterface.BUTTON_NEGATIVE);
            }
        }
    }
}
