package com.tantra.tantrayoga.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.loader.content.Loader;

public abstract class BaseCursorLoader extends BaseLocalLoader<Cursor> {

    private Loader.ForceLoadContentObserver observer = new ForceLoadContentObserver();

    public BaseCursorLoader(Context context) {
        super(context);
    }

    public BaseCursorLoader(Context context, Uri contentUri) {
        super(context, contentUri);
    }

    @Override
    protected void onReleaseResources(Cursor data, boolean wasDelivered) {
        if (!wasDelivered && data != null) {
            data.close();
        }
    }

    @Override
    public final Cursor loadInBackground() {
        Cursor cursor = loadCursorInBackground();

        if (cursor != null) {
            cursor.getCount();
            cursor.registerContentObserver(observer);
        }
        return cursor;
    }

    protected abstract Cursor loadCursorInBackground();
}
