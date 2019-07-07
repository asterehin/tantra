package com.tantra.tantrayoga.common.dependencyinjection.logging;

import android.util.Log;

/**
 * This class is a non-static logger wrapper
 */
public class MyLogger {

    public void e(String tag, String message, Object... args) {
        Log.e(tag, message);
    }

    public void d(String tag, String message, Object... args) {
//        Timber.tag(tag);
//        Timber.d(message, args);
        Log.d(tag, message);

    }

    public void v(String tag, String message, Object... args) {
//        Timber.tag(tag);
//        Timber.v(message, args);
        Log.v(tag, message);

    }

}
