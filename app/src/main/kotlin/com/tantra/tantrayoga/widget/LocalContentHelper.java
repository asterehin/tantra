package com.tantra.tantrayoga.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;;import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tantra.tantrayoga.BuildConfig;

public final class LocalContentHelper {

    private static final String ACTION_REFRESH_CONTENT = BuildConfig.APPLICATION_ID + ".REFRESH_CONTENT";

    private LocalContentHelper() {
    }

    /**
     * Sends a notification that will invoke any registered receivers
     * according to is content uri or its descendants
     *
     * @param context    is the context
     * @param contentUri that identifies the content
     */
    public static void notifyContentChange(Context context, Uri contentUri) {
        checkUri(contentUri);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(ACTION_REFRESH_CONTENT, contentUri);
        //intent.putExtra(EXTRA_CONTENT_URI, contentUri);
        bm.sendBroadcast(intent);
    }

    /**
     * Register a receiver instance to be notified when certain data is changed.
     * The receiver will also be invoked if any notification arrives to the parent content uri.
     *
     * @param context    is the context
     * @param receiver   that will be called when a notification arrives
     * @param contentUri that identifies the content
     */
    public static void registerForContentChanges(Context context, BroadcastReceiver receiver,
                                                 Uri contentUri) {
//        ObjectsUtils.checkNull(receiver);
        checkUri(contentUri);
        final IntentFilter filter = new IntentFilter(ACTION_REFRESH_CONTENT);
        filter.addDataScheme(contentUri.getScheme());
        filter.addDataAuthority(contentUri.getHost(), Integer.toString(contentUri.getPort()));
        filter.addDataPath(contentUri.getPath(), PatternMatcher.PATTERN_PREFIX);
        final LocalBroadcastManager bm = LocalBroadcastManager.getInstance(context);
        bm.registerReceiver(receiver, filter);
    }

    /**
     * @param context  is the context
     * @param receiver that is currently registered
     */
    public static void unregisterForContentChanges(Context context, BroadcastReceiver receiver) {
//        ObjectsUtils.checkNull(receiver);
        final LocalBroadcastManager bm = LocalBroadcastManager.getInstance(context);
        bm.unregisterReceiver(receiver);
    }

    private static void checkUri(Uri contentUri) {
//        ObjectsUtils.checkNull(contentUri);
        if (Uri.EMPTY == contentUri) {
            throw new IllegalArgumentException("contentUri cannot be empty");
        }
    }

}
