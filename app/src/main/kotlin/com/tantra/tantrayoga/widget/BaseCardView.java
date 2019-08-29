package com.tantra.tantrayoga.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tantra.tantrayoga.R;

public class BaseCardView extends FrameLayout {

    private static final int[] THEME_ATTRIBUTES = new int[]{
            R.attr.cardStatusWidth,
            R.attr.cardStatusLargeWidth
    };

    private static final int DEFAULT_STATUS_W_WITH_STATUS = 8;
    private static final int DEFAULT_STATUS_W_WITH_LARGE_STATUS = 20;

    private static final int DEFAULT_STATUS_COLOR = Color.BLUE;
    private static final int DEFAULT_CARD_COLOR = Color.WHITE;

    private boolean withStatus = false;
    private boolean withLargeStatus = false;

    private int statusColor;
    private int cardColor;

    @Nullable
    private ColorStateList backgroundColorStateList;

    private CardView innerCardView;
    private FrameLayout contentFrameView;
    private ImageView iconView;
    private TextView labelView;

    private int statusWidth;

    private int normalStatusWidth;
    private int largeStatusWidth;

    public BaseCardView(Context context) {
        this(context, null, 0);
    }

    public BaseCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        // Load theme values

        TypedArray themeAttributes = context.obtainStyledAttributes(THEME_ATTRIBUTES);
        normalStatusWidth = themeAttributes.getDimensionPixelSize(0, DEFAULT_STATUS_W_WITH_STATUS);
        largeStatusWidth = themeAttributes
                .getDimensionPixelSize(1, DEFAULT_STATUS_W_WITH_LARGE_STATUS);
        themeAttributes.recycle();

        // Load attributes
        TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.BaseCardView, defStyle, defStyleRes);

        boolean withStatus = a.getBoolean(R.styleable.BaseCardView_withStatus, false);
        boolean withLargeStatus = a.getBoolean(R.styleable.BaseCardView_withBigStatus, false);

        backgroundColorStateList = a.getColorStateList(R.styleable.BaseCardView_cardBackground);
        boolean hasStatusColor = a.hasValue(R.styleable.BaseCardView_statusColor);
        int statusColor = a.getColor(R.styleable.BaseCardView_statusColor, DEFAULT_STATUS_COLOR);

        int iconResId = a.getResourceId(R.styleable.BaseCardView_statusIcon, 0);
        CharSequence labelText = a.getText(R.styleable.BaseCardView_statusLabel);

        Drawable foreground = a.getDrawable(R.styleable.BaseCardView_cardForeground);

        withLargeStatus |= iconResId != 0 || !TextUtils.isEmpty(labelText);

        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.mcm_card, this, true);

        innerCardView = (CardView) getChildAt(0);
        contentFrameView = (FrameLayout) innerCardView.findViewById(R.id.mcm_card_content);
        iconView = (ImageView) innerCardView.findViewById(R.id.mcm_card_icon);
        labelView = (TextView) innerCardView.findViewById(R.id.mcm_card_label);

        if (hasStatusColor) {
            setStatusColor(statusColor);
        }

        if (backgroundColorStateList != null) {
            setCardBackgroundColor(backgroundColorStateList.getDefaultColor());
        } else {
            setCardBackgroundColor(DEFAULT_CARD_COLOR);
        }

        if (foreground != null) {
            setCardForeground(foreground);
        }

        if (withLargeStatus) {
            setWithLargeStatus(true);
        } else if (withStatus) {
            setWithStatus(true);
        }

        updateStatusWidth();

        setStatusIcon(iconResId);
        setStatusLabel(labelText);
    }

    private void updateStatusWidth() {
        statusWidth = withStatus ? (withLargeStatus ? largeStatusWidth : normalStatusWidth) : 0;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (contentFrameView == null) {
            super.addView(child, index, params);
        } else {
            contentFrameView.addView(child, index, params);
        }
    }

    public void setStatusColor(@ColorInt int statusColor) {
        if (this.statusColor != statusColor) {
            setWithStatus(true);
            innerCardView.setCardBackgroundColor(statusColor);
            this.statusColor = statusColor;
        }
    }

    public void setStatusColorResource(@ColorRes int colorRes) {
        setStatusColor(ContextCompat.getColor(getContext(), colorRes));
    }

    private void setWithStatus(boolean withStatus) {
        if (this.withStatus != withStatus) {
            this.withStatus = withStatus;
            updateStatusWidth();
            updateStatus();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MarginLayoutParams lp = (MarginLayoutParams) labelView.getLayoutParams();
        lp.height = 0;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int additionalTopMargin;
        if (iconView.getVisibility() == VISIBLE) {
            additionalTopMargin = iconView.getMeasuredHeight();
        } else {
            additionalTopMargin = labelView.getPaddingBottom();
        }

        int labelWidthMeasureSpec = labelView.getMeasuredWidthAndState();
        int height = Math.max(0, innerCardView.getMeasuredHeight()
                - innerCardView.getPaddingTop() - innerCardView.getPaddingBottom()
                - lp.topMargin - lp.bottomMargin - additionalTopMargin);
        int labelHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                                                                 MeasureSpec.EXACTLY);
        labelView.measure(labelWidthMeasureSpec, labelHeightMeasureSpec);
        labelView.requestLayout();
    }

    private void setWithLargeStatus(boolean withLargeStatus) {
        if (this.withLargeStatus != withLargeStatus) {
            this.withStatus |= withLargeStatus;
            this.withLargeStatus = withLargeStatus;
            updateStatusWidth();
            updateStatus();
        }
    }

    public void setCardBackgroundColor(@ColorInt int cardBackgroundColor) {
        if (this.cardColor != cardBackgroundColor) {
            contentFrameView.setBackgroundColor(cardBackgroundColor);
            cardColor = cardBackgroundColor;
        }
    }

    public void setCardBackgroundColorResource(@ColorRes int colorRes) {
        backgroundColorStateList = ContextCompat.getColorStateList(getContext(), colorRes);
        updateCardBackgroundColor();
    }

    private void setCardForeground(Drawable foreground) {
        contentFrameView.setForeground(foreground);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (backgroundColorStateList != null && backgroundColorStateList.isStateful()) {
            updateCardBackgroundColor();
        }
    }

    private void updateCardBackgroundColor() {
        if (backgroundColorStateList != null) {
            int color = backgroundColorStateList.getColorForState(getDrawableState(), 0);
            setCardBackgroundColor(color);
        }
    }

    public void setStatusIcon(@DrawableRes int iconResId) {
        if (iconResId == 0) {
            iconView.setVisibility(View.GONE);
        } else {
            if (withLargeStatus) {
                iconView.setVisibility(View.VISIBLE);
                iconView.setImageResource(iconResId);
            }
        }
    }

    public void setStatusIcon(Drawable drawable) {
        if (drawable == null) {
            iconView.setVisibility(View.GONE);
        } else {
            if (withLargeStatus) {
                iconView.setVisibility(View.VISIBLE);
                iconView.setImageDrawable(drawable);
            }
        }
    }

    public void setStatusLabel(@StringRes int labelResId) {
        if (labelResId == 0) {
            labelView.setVisibility(View.GONE);
        } else {
            if (withLargeStatus) {
                labelView.setVisibility(View.VISIBLE);
                labelView.setText(labelResId);
            }
        }
    }

    public void setStatusLabel(CharSequence statusLabel) {
        String label = null;
        if (statusLabel != null) {
            label = statusLabel.toString();
        }
        setStatusLabel(label);
    }

    public void setStatusLabel(String statusLabel) {
        if (TextUtils.isEmpty(statusLabel)) {
            labelView.setVisibility(View.GONE);
        } else {
            if (withLargeStatus) {
                labelView.setVisibility(View.VISIBLE);
                labelView.setText(statusLabel);
            }
        }
    }

    private void updateStatus() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentFrameView
                .getLayoutParams();
        if (withStatus) {
            params.setMargins(statusWidth, 0, 0, 0);
        } else {
            params.setMargins(0, 0, 0, 0);
        }
        contentFrameView.setLayoutParams(params);
    }

    @VisibleForTesting
    boolean isWithStatus() {
        return withStatus;
    }

}
