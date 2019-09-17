package com.tantra.tantrayoga.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public abstract class BaseLocalLoader<T> extends AbstractAsyncTaskLoader<T> {

    private LocalBroadcastReceiver receiver;
    private Uri contentUri;

    public BaseLocalLoader(Context context) {
        super(context);
    }

    public BaseLocalLoader(Context context, final Uri contentUri) {
        super(context);
        if (contentUri == null) {
            throw new IllegalArgumentException("'contentUri' can not be null");
        }
        this.contentUri = contentUri;
    }

    @Override
    protected void onCreateContentObserver() {
        if (receiver == null && contentUri != null) {
            receiver = new LocalBroadcastReceiver();
            LocalContentHelper.registerForContentChanges(getContext(), receiver, contentUri);
        }
    }

    @Override
    protected void onDestroyContentObserver() {
        if (receiver != null) {
            LocalContentHelper.unregisterForContentChanges(getContext(), receiver);
            receiver = null;
        }
    }

    public static void notifyContentChange(Context context, Uri contentUri) {
        LocalContentHelper.notifyContentChange(context, contentUri);
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {

        LocalBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            onContentChanged();
        }
    }
}
