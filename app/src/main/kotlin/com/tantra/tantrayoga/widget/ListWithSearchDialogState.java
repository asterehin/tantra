package com.tantra.tantrayoga.widget;

public interface ListWithSearchDialogState<T> extends IdentifiableDialogState {
    T getSelectedItem();

    boolean hasSelectedItem();
}
