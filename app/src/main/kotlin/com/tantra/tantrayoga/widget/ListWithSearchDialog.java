package com.tantra.tantrayoga.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;


import com.tantra.tantrayoga.R;
import com.tantra.tantrayoga.utils.GenericUtils;

import java.io.Serializable;

public class ListWithSearchDialog extends IdentifiableDialogFragment<ListWithSearchDialogState>
        implements ListWithSearchDialogState, ListWithSearchFragment.OnItemSelectedListener {

    protected static final String SELECTED_ITEM_KEY = "selectedItem";

    protected static final String OTHER_ACTION_LABEL_ARG = "otherActionLabel";
    protected static final String SELECTION_SEARCHABLE_FACTORY_ARG = "selectionSearchableFactory";
    protected static final String SELECTION_SEARCHABLE_FACTORY_IS_PARCELABLE_ARG = "selectionSearchableFactoryIsParcelable";
    protected static final String SELECTION_SEARCHABLE_FACTORY_WITH_PARAMETER_ARG = "selectionSearchableFactoryWithParameter";

    protected static Bundle createArgsBundle(String title, int listType, Factory<?, ?> factory,
                                             String otherActionLabel,
                                             FactoryWithParameter<?, ?, ?> factoryWithParameter) {
        boolean isParcelable = checkParcelableType(factory);
        Bundle args = new Bundle();
        args.putString(TITLE_ARG, title);
        args.putInt(DIALOG_SUB_TYPE_ARG, listType);
        args.putSerializable(SELECTION_SEARCHABLE_FACTORY_ARG, factory);
        args.putBoolean(SELECTION_SEARCHABLE_FACTORY_IS_PARCELABLE_ARG, isParcelable);
        args.putSerializable(SELECTION_SEARCHABLE_FACTORY_WITH_PARAMETER_ARG, factoryWithParameter);
        args.putString(OTHER_ACTION_LABEL_ARG, otherActionLabel);
        return args;
    }

    public static ListWithSearchDialog newInstance(String title, int listType,
                                                   Factory<?, ?> factory, String otherActionLabel) {
        ListWithSearchDialog fragment = new ListWithSearchDialog();
        Bundle args = createArgsBundle(title, listType, factory, otherActionLabel, null);
        fragment.setArguments(args);
        return fragment;
    }

    protected static boolean checkParcelableType(Factory<?, ?> factory) {
        Class<?> itemClass = factory.itemType;

        boolean result = Parcelable.class.isAssignableFrom(itemClass);
        if (!result && !Serializable.class.isAssignableFrom(itemClass)) {
            throw new IllegalArgumentException(
                    "Factory item type needs to be Serializable or Parcelable");
        }
        return result;
    }

    private Object selectedItem;

    private boolean itemTypeIsParcelable;

    private Factory<?, ?> selectionSearchableFactory;
    private FactoryWithParameter<?, ?, ?> selectionSearchableFactoryWithParameter;
    private String otherActionLabel;

    private ViewPager viewPager;

    private View ok;
    private Button cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        selectionSearchableFactory = (Factory<?, ?>) args
                .getSerializable(SELECTION_SEARCHABLE_FACTORY_ARG);
        selectionSearchableFactoryWithParameter = (FactoryWithParameter<?, ?, ?>) args
                .getSerializable(SELECTION_SEARCHABLE_FACTORY_WITH_PARAMETER_ARG);
        itemTypeIsParcelable = args.getBoolean(SELECTION_SEARCHABLE_FACTORY_IS_PARCELABLE_ARG);
        otherActionLabel = args.getString(OTHER_ACTION_LABEL_ARG);

        if (savedInstanceState != null) {
            if (itemTypeIsParcelable) {
                selectedItem = savedInstanceState.getParcelable(SELECTED_ITEM_KEY);
            } else {
                selectedItem = savedInstanceState.getSerializable(SELECTED_ITEM_KEY);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mcm_list_with_search_dialog, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.mcm_list_viewpager);

        ListWithSearchPagerAdapter listWithSearchPagerAdapter = new ListWithSearchPagerAdapter(
                getChildFragmentManager(), selectionSearchableFactory,
                selectionSearchableFactoryWithParameter);
        viewPager.setAdapter(listWithSearchPagerAdapter);

        ok = v.findViewById(R.id.dialog_ok);
        cancel = (Button) v.findViewById(R.id.dialog_cancel);

        View.OnClickListener okOnButtonClickListener = new ListWithSearchDialog.OnButtonClickListener();
        ok.setOnClickListener(okOnButtonClickListener);
        cancel.setOnClickListener(okOnButtonClickListener);

        if (!TextUtils.isEmpty(otherActionLabel)) {
            Button otherButton = (Button) v.findViewById(R.id.dialog_other);
            otherButton.setVisibility(View.VISIBLE);
            otherButton.setText(otherActionLabel);
            otherButton.setOnClickListener(okOnButtonClickListener);
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasFactoryWithParameter() && viewPager.getCurrentItem() == 0) {
            ok.setVisibility(View.GONE);
        }
        if (hasFactoryWithParameter() && viewPager.getCurrentItem() == 1) {
            cancel.setText(R.string.dialog_back);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (itemTypeIsParcelable) {
            outState.putParcelable(SELECTED_ITEM_KEY, (Parcelable) selectedItem);
        } else {
            outState.putSerializable(SELECTED_ITEM_KEY, (Serializable) selectedItem);
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public boolean hasSelectedItem() {
        return selectedItem != null;
    }

    @Override
    public void onItemSelected(Object item) {
        if (hasFactoryWithParameter() && viewPager.getCurrentItem() != 1) {
            viewPager.setCurrentItem(1);
            sendParameterSelectedBroadcast(item);
            ok.setVisibility(View.VISIBLE);
            cancel.setText(R.string.dialog_back);
            selectedItem = null;
        } else {
            selectedItem = item;
        }
    }

    private boolean hasFactoryWithParameter() {
        return selectionSearchableFactoryWithParameter != null && selectionSearchableFactoryWithParameter
                .hasParameter();
    }

    private void sendParameterSelectedBroadcast(Object item) {
        Intent intent = new Intent(ListWithSearchFragment.ON_PARAMETER_SELECTED_ACTION);
        if (itemTypeIsParcelable) {
            intent.putExtra(ListWithSearchFragment.PARAMETER_KEY,
                            (Parcelable) item);
        } else {
            intent.putExtra(ListWithSearchFragment.PARAMETER_KEY,
                            (Serializable) item);
        }
        intent.putExtra(ListWithSearchFragment.PARAMETER_IS_PARCELABLE_KEY,
                        itemTypeIsParcelable);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private class OnButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.dialog_ok) {
                GenericUtils.hideSoftKeyboard(getContext(), v, true);
                dispatchOnButtonClick(ListWithSearchDialog.this,
                                      DialogInterface.BUTTON_POSITIVE);
            } else if (viewId == R.id.dialog_cancel) {
                GenericUtils.hideSoftKeyboard(getContext(), v, true);
                if (hasFactoryWithParameter() && viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                    cancel.setText(R.string.dialog_cancel);
                    ok.setVisibility(View.GONE);
                    selectedItem = null;
                } else {
                    dispatchOnButtonClick(ListWithSearchDialog.this,
                                          DialogInterface.BUTTON_NEGATIVE);
                }
            } else if (viewId == R.id.dialog_other) {
                dispatchOnButtonClick(ListWithSearchDialog.this, DialogInterface.BUTTON_NEUTRAL);
            }
        }
    }

    public abstract static class Factory<VH extends ListWithSearchDialogItemAdapter.ViewHolder, T> implements Serializable {

        final Class<T> itemType;

        protected Factory(Class<T> itemType) {
            this.itemType = itemType;
        }

        public abstract ListWithSearchDialogItemAdapter<VH> createAdapter(Context context);

        public abstract Loader<Cursor> createLoader(Context context, String search);

        public abstract T createItem(Cursor cursor);

        protected boolean hasParameter() {
            return false;
        }
    }

    public abstract static class FactoryWithParameter<VH extends ListWithSearchDialogItemAdapter.ViewHolder, T, U>
            extends Factory<VH, T> {

        protected FactoryWithParameter(Class<T> itemType) {
            super(itemType);
        }

        public abstract Loader<Cursor> createLoader(Context context, String search, U parameter);

        final public Loader<Cursor> createLoader(Context context, String search) {
            return createLoader(context, search, null);
        }

        final protected boolean hasParameter() {
            return true;
        }
    }

    public abstract static class BaseFactory<T> extends Factory<ListWithSearchDialogItemAdapter.ViewHolder, T> {

        protected BaseFactory(Class<T> itemType) {
            super(itemType);
        }

        @Override
        public ListWithSearchDialogItemAdapter<ListWithSearchDialogItemAdapter.ViewHolder> createAdapter(
                Context context) {
            return new ListWithSearchDialogItemAdapter<>(context);
        }
    }

    private static class ListWithSearchPagerAdapter extends FragmentPagerAdapter {

        private Factory<?, ?> selectionSearchableFactory;
        private FactoryWithParameter<?, ?, ?> selectionSearchableFactoryWithParameter;

        ListWithSearchPagerAdapter(FragmentManager fm,
                                   Factory<?, ?> selectionSearchableFactory,
                                   FactoryWithParameter<?, ?, ?> selectionSearchableFactoryWithParameter) {
            super(fm);
            this.selectionSearchableFactory = selectionSearchableFactory;
            this.selectionSearchableFactoryWithParameter = selectionSearchableFactoryWithParameter;
        }

        @Override
        public int getCount() {
            return selectionSearchableFactoryWithParameter == null ? 1 : 2;
        }

        @Override
        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    return ListWithSearchFragment.newInstance(selectionSearchableFactory );
//                case 1:
//                    return ListWithSearchFragment
//                            .newInstance(selectionSearchableFactoryWithParameter);
//                default:
                    return null;
//            }
        }
    }
}
