package com.tantra.tantrayoga.widget;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;

import androidx.recyclerview.widget.RecyclerView;

public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final Context context;
    protected Cursor cursor;

    private boolean dataValid;
    private int rowIdColumn;

    private DataSetObserver dataSetObserver;

    /**
     * If {@code registerContentObserver} is set to true the adapter will register a
     * content observer on the cursor and will call {@link #notifyDataSetChanged()}
     * when a notification comes in.
     * <p>
     * Be careful when using this flag: you will need to call {@link #clean()} that unset the cursor
     * to avoid leaks due to its registered observers.
     * <p>
     * The {@code registerContentObserver} should be false if you are using a Loader.
     */
    public CursorRecyclerViewAdapter(Context context, Cursor cursor,
                                     boolean registerContentObserver) {
        this.context = context;
        this.cursor = cursor;
        this.dataValid = cursor != null;

        if (registerContentObserver) {
            this.dataSetObserver = new NotifyingDataSetObserver();
        }
        if (this.cursor != null && dataSetObserver != null) {
            this.cursor.registerDataSetObserver(dataSetObserver);
        }

        updateColumnIndexes();
    }

    public CursorRecyclerViewAdapter(Context context, Cursor cursor) {
        this(context, cursor, false);
    }

    public CursorRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    protected String getRowIdColumnName() {
        return BaseColumns._ID;
    }

    protected void updateColumnIndexes() {
        this.rowIdColumn = dataValid ? this.cursor
                .getColumnIndex(getRowIdColumnName()) : RecyclerView.NO_POSITION;
    }

    protected Cursor getCursor() {
        return cursor;
    }

    protected int getRowIdColumn() {
        return rowIdColumn;
    }

    @Override
    public int getItemCount() {
        if (dataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(rowIdColumn);
        }
        return RecyclerView.NO_ID;
    }

    public abstract void onBindViewHolder(VH viewHolder, int position, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(viewHolder, position, cursor);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
            dataValid = true;
            updateColumnIndexes();
            notifyDataSetChanged();
        } else {
            dataValid = false;
            updateColumnIndexes();
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    public void clean() {
        if (cursor != null && dataSetObserver != null) {
            cursor.unregisterDataSetObserver(dataSetObserver);
        }
        cursor = null;
        dataValid = false;
        updateColumnIndexes();
    }

    private class NotifyingDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            dataValid = true;
            notifyDataSetChanged();

        }

        @Override
        public void onInvalidated() {
            dataValid = false;
            notifyDataSetChanged();
        }
    }

}
