package com.tantra.tantrayoga.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tantra.tantrayoga.R;

public final class LoadingDelegate implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LOADING_ARG = "loading";
    private static final String EMPTY_ARG = "empty";

    private ProgressBar contentLoadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View contentView;
    private TextView emptyView;

    boolean loading;
    private boolean empty;

    private boolean withEmpty;
    private boolean withPullToRefresh;

    private OnRefreshListener listener;

    public interface OnRefreshListener {
        void onRefresh();
    }

    private LoadingDelegate(boolean withEmpty, boolean withPullToRefresh) {
        this.withEmpty = withEmpty;
        this.withPullToRefresh = withPullToRefresh;
    }

    public static LoadingDelegate create() {
        return new LoadingDelegate(false, false);
    }

    public static LoadingDelegate create(Context context, boolean withEmpty) {
        return new LoadingDelegate(withEmpty, false);
    }

    public static LoadingDelegate create(Context context, boolean withEmpty,
                                         boolean withPullToRefresh) {
        return new LoadingDelegate(withEmpty, withPullToRefresh);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
        updateSwipeRefreshLayout();
    }

    private void updateSwipeRefreshLayout() {
        if (swipeRefreshLayout != null) {
            if (listener == null) {
                swipeRefreshLayout.setEnabled(false);
            } else {
                swipeRefreshLayout.setEnabled(true);
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            loading = savedInstanceState.getBoolean(LOADING_ARG);
            empty = savedInstanceState.getBoolean(EMPTY_ARG);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(LOADING_ARG, loading);
        outState.putBoolean(EMPTY_ARG, empty);
    }

    @Override
    public void onRefresh() {
        OnRefreshListener listener = this.listener;
        if (listener != null) {
            this.loading = true;
            listener.onRefresh();
        } else if (swipeRefreshLayout != null) { // impossible to happen
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public View wrapView(View content) {
        return wrapView(content, content);
    }

    public View wrapView(View rootView, @IdRes int contentId) {
        View contentView = rootView.findViewById(contentId);
        if (contentView == null) {
            throw new IllegalArgumentException("contentId is not valid");
        }

        return wrapView(rootView, contentView);
    }

    public View wrapView(View rootView, View contentView) {
        Context context = rootView.getContext();
        this.contentView = contentView;
        boolean innerContent = rootView != contentView;

        if (withPullToRefresh) {
            initSwipeRefreshLayout(context);
        } else {
            initLoadingProgressBar(context);
        }

        ViewGroup wrappingLayout = new FrameLayout(context);
        ViewGroup.LayoutParams rootViewParams = rootView.getLayoutParams();
        if (rootViewParams == null) {
            wrappingLayout.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                               ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            wrappingLayout.setLayoutParams(rootViewParams);
        }
        wrappingLayout.addView(rootView,
                               new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.MATCH_PARENT));

        ViewGroup contentFrame;
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (innerContent) {
            ViewGroup.LayoutParams contentViewParams = contentView.getLayoutParams();
            int index = parent.indexOfChild(contentView);
            // prevents the view to stay "attached" to parent caused by its layout transition
            LayoutTransition parentLayoutTransition = parent.getLayoutTransition();
            if (parentLayoutTransition != null) {
                parent.setLayoutTransition(null);
            }

            parent.removeViewAt(index);

            contentFrame = new FrameLayout(context);
            contentFrame.setLayoutParams(contentViewParams);

            if (withPullToRefresh) {
                swipeRefreshLayout.addView(contentView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                contentFrame.addView(swipeRefreshLayout, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                contentFrame.addView(contentView, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                contentFrame.addView(contentLoadingProgressBar, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER_HORIZONTAL | Gravity.TOP));
            }
            parent.addView(contentFrame, index);

            if (parentLayoutTransition != null) {
                parent.setLayoutTransition(parentLayoutTransition);
            }
        } else {
            if (withPullToRefresh) {
                swipeRefreshLayout.addView(wrappingLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                contentFrame = swipeRefreshLayout;
            } else {
                contentFrame = wrappingLayout;
                contentFrame.addView(contentLoadingProgressBar, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER_HORIZONTAL | Gravity.TOP));
            }
        }

        if (withEmpty) {
            initEmptyView(context, contentFrame);
            updateEmptyViewVisibility();
        }

        updateLoadingIndicator();
        updateContentVisibility();
        updateSwipeRefreshLayout();

        return wrappingLayout;
    }

    private void initEmptyView(Context context, ViewGroup contentFrame) {
        int paddingSize = contentView.getResources()
                .getDimensionPixelSize(R.dimen.mcm_activity_vertical_margin) * 2;
        TextView emptyView = new TextView(context);
        emptyView.setText(R.string.no_items_to_display);
        emptyView.setPadding(0, paddingSize, 0, 0);
        contentFrame.addView(emptyView,
                             new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                          ViewGroup.LayoutParams.WRAP_CONTENT,
                                                          Gravity.CENTER_HORIZONTAL | Gravity.TOP));
        this.emptyView = emptyView;
    }

    private void initLoadingProgressBar(Context context) {
        int paddingSize = contentView.getResources()
                .getDimensionPixelSize(R.dimen.mcm_activity_vertical_margin) * 2;
        contentLoadingProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        contentLoadingProgressBar.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        contentLoadingProgressBar.setIndeterminate(true);
    }

    private void initSwipeRefreshLayout(Context context) {
        swipeRefreshLayout = new SwipeRefreshLayout(context);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout
                .setColorSchemeResources(R.color.coins_flat_blue, R.color.coins_flat_orange,
                                         R.color.coins_flat_purple, R.color.coins_flat_light_blue);
    }

    private void updateLoadingIndicator() {
        if (contentLoadingProgressBar != null) {
            contentLoadingProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        if (swipeRefreshLayout != null) {
            final SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(loading);
                }
            });
        }
    }

    private void updateContentVisibility() {
        if (contentView != null) {
            contentView.setVisibility(empty || loading ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private void updateEmptyViewVisibility() {
        if (emptyView != null) {
            emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        }
    }

    public void show() {
        if (!loading) {
            loading = true;
            empty = false;
            updateLoadingIndicator();
            updateEmptyViewVisibility();
            updateContentVisibility();
        }
    }

    public void hide(boolean empty) {
        if (loading) {
            loading = false;
            updateLoadingIndicator();
        }

        this.empty = empty;

        updateEmptyViewVisibility();
        updateContentVisibility();
    }

}
