package com.tantra.tantrayoga.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tantra.tantrayoga.R;


public class ListWithSearchDialogItemAdapter<VH extends ListWithSearchDialogItemAdapter.ViewHolder> extends CursorRecyclerViewAdapter<VH> implements OnItemClickListener<Integer> {

    private int codeColumn;
    private int descriptionColumn;

    private final String codeColumnName;
    private final String descriptionName;

    private final boolean showCodeColumn;

    @LayoutRes
    private final int itemLayoutRes;

    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnSelectionChangedListener listener;

    public ListWithSearchDialogItemAdapter(Context context,
                                           String codeColumnName, String descriptionName,
                                           boolean showCodeColumn) {
        super(context);
        this.codeColumnName = codeColumnName;
        this.descriptionName = descriptionName;
        this.showCodeColumn = showCodeColumn;

        this.itemLayoutRes = showCodeColumn ?
                             R.layout.mcm_list_dialog_item :
                             R.layout.mcm_list_dialog_item_no_code;
    }

    public ListWithSearchDialogItemAdapter(Context context,
                                           String codeColumnName,
                                           String descriptionName) {
        this(context, codeColumnName, descriptionName, true);
    }

    public ListWithSearchDialogItemAdapter(Context context) {
        this(context, "name",
             "desc");
    }

    @Override
    protected void updateColumnIndexes() {
        super.updateColumnIndexes();

        super.updateColumnIndexes();
        if (cursor != null) {
            codeColumn = cursor.getColumnIndex(codeColumnName);
            descriptionColumn = cursor.getColumnIndex(descriptionName);
        } else {
            codeColumn = -1;
            descriptionColumn = -1;
        }
    }

    public void setOnSelectionChangedListener(
            OnSelectionChangedListener onSelectionChangedListener) {
        this.listener = onSelectionChangedListener;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public boolean hasSelectedItem() {
        return selectedPosition != RecyclerView.NO_POSITION;
    }

    public void toggleSelected(int position) {
        if (this.selectedPosition != position) {
            if (this.selectedPosition != RecyclerView.NO_POSITION) {
                // old position
                this.notifyItemChanged(this.selectedPosition);
            }
            this.selectedPosition = position;
            this.notifyItemChanged(this.selectedPosition);
        } else {
            if (this.selectedPosition != RecyclerView.NO_POSITION) {
                this.notifyItemChanged(this.selectedPosition);
            }
        }
    }

    private void dispatchListener() {
        if (listener != null) {
            listener.onSelectionChanged(selectedPosition);
        }
    }

    @Override
    public void onViewRecycled(VH holder) {
        holder.onItemClickListener = null;
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = super.swapCursor(newCursor);
        selectedPosition = RecyclerView.NO_POSITION;
        return oldCursor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayoutRes, parent, false);
        return (VH) new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position, Cursor cursor) {
        if (viewHolder.codeView != null) {
            viewHolder.codeView.setText(cursor.getString(codeColumn));
        }
        if (viewHolder.descriptionView != null) {
            viewHolder.descriptionView.setText(cursor.getString(descriptionColumn));
        }
        viewHolder.setSelected(position == selectedPosition);
        viewHolder.onItemClickListener = this;
    }

    protected int getCodeColumnIndex() {
        return codeColumn;
    }

    protected int getDescriptionColumnIndex() {
        return descriptionColumn;
    }

    protected String getCodeColumnName() {
        return codeColumnName;
    }

    protected String getDescriptionName() {
        return descriptionName;
    }

    protected boolean isShowCodeColumnEnabled() {
        return showCodeColumn;
    }

    @Override
    public void onItemClick(Integer position) {
        toggleSelected(position);
        dispatchListener();
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final View itemView;
        @Nullable
        final TextView codeView;
        @Nullable
        final TextView descriptionView;
        OnItemClickListener<Integer> onItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.codeView = (TextView) itemView.findViewById(R.id.list_dialog_code);
            this.descriptionView = (TextView) itemView.findViewById(R.id.list_dialog_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }

        protected void setSelected(boolean selected) {
            this.itemView.setActivated(selected);
        }
    }
}
