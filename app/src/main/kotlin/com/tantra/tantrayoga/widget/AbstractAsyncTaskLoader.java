package com.tantra.tantrayoga.widget;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

public abstract class AbstractAsyncTaskLoader<T> extends AsyncTaskLoader<T> {

    private T data;

    public AbstractAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            if (this.data != null) {
                onReleaseResources(this.data, false);
            }
        }
        T oldData = this.data;
        this.data = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null) {
            onReleaseResources(oldData, true);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (this.data != null) {
            deliverResult(this.data);
        }

        onCreateContentObserver();

        if (takeContentChanged() || this.data == null) {
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(T Data) {
        super.onCanceled(data);
        onReleaseResources(data, false);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();
        if (this.data != null) {
            onReleaseResources(this.data, false);
            this.data = null;
        }

        onDestroyContentObserver();
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     * <p>
     * Implement this to release data resources
     */
    protected void onReleaseResources(T data, boolean wasDelivered) {
    }

    protected abstract void onCreateContentObserver();

    protected abstract void onDestroyContentObserver();
}
