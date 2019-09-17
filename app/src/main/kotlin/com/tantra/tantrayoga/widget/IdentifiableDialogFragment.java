package com.tantra.tantrayoga.widget;


import android.os.Bundle;

public abstract class IdentifiableDialogFragment<T> extends BaseDialogFragment<T> {

    protected static final String DIALOG_SUB_TYPE_ARG = "subType";

    private int subType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        subType = args.getInt(DIALOG_SUB_TYPE_ARG, -1);
    }

    public int getDialogSubType() {
        return subType;
    }
}
