package com.tantra.tantrayoga.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tantra.tantrayoga.BuildConfig;
import com.tantra.tantrayoga.R;
import com.tantra.tantrayoga.utils.GenericUtils;

import java.io.Serializable;

public class ListWithSearchFragment extends DialogFragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchView.OnQueryTextListener, ListWithSearchDialogItemAdapter.OnSelectionChangedListener,
        SearchView.OnCloseListener, BaseFlowController {

    public static final String TAG = "ListWithSearchFragment";

    public interface OnItemSelectedListener {
        void onItemSelected(Object item);
    }

    protected static final String ON_PARAMETER_SELECTED_ACTION = BuildConfig.APPLICATION_ID + ".ListWithSearchFragment.OnParameterSelectedAction";

    protected static final String PARAMETER_KEY = "parameterItem";
    protected static final String PARAMETER_IS_PARCELABLE_KEY = "parameterIsParcelable";

    protected static final String SELECTED_ITEM_KEY = "selectedItem";
    protected static final String SELECTED_POSITION_KEY = "selectedPosition";
    protected static final String SEARCH_TEXT_KEY = "searchText";

    protected static final String SELECTION_SEARCHABLE_FACTORY_ARG = "selectionSearchableFactory";
    protected static final String SELECTION_SEARCHABLE_FACTORY_IS_PARCELABLE_ARG = "selectionSearchableFactoryIsParcelable";

    protected static Bundle createArgsBundle(ListWithSearchDialog.Factory<?, ?> factory) {
        boolean isParcelable = ListWithSearchDialog.checkParcelableType(factory);
        Bundle args = new Bundle();
        args.putSerializable(SELECTION_SEARCHABLE_FACTORY_ARG, factory);
        args.putBoolean(SELECTION_SEARCHABLE_FACTORY_IS_PARCELABLE_ARG, isParcelable);
        return args;
    }

    public static ListWithSearchFragment newInstance(ListWithSearchDialog.Factory<?, ?> factory, OnItemSelectedListener onItemSelectedListener) {
        ListWithSearchFragment fragment = new ListWithSearchFragment();
        Bundle args = createArgsBundle(factory);
        fragment.setArguments(args);
        fragment.onItemSelectedListener = onItemSelectedListener;
        return fragment;
    }

    private BroadcastReceiver onParameterSelectedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            parameterTypeIsParcelable = intent.getBooleanExtra(PARAMETER_IS_PARCELABLE_KEY, false);
            if (parameterTypeIsParcelable) {
                parameter = intent.getParcelableExtra(PARAMETER_KEY);
            } else {
                parameter = intent.getSerializableExtra(PARAMETER_KEY);
            }
            selectedItem = null;
            selectedPosition = RecyclerView.NO_POSITION;
            getLoaderManager().restartLoader(0, null, ListWithSearchFragment.this);
        }
    };

    private int selectedPosition = RecyclerView.NO_POSITION;

    private Object selectedItem;

    private Object parameter;
    private boolean parameterTypeIsParcelable;

    private boolean restoreAdapter = false;
    private boolean itemTypeIsParcelable;

    private ListWithSearchDialog.Factory<?, ?> selectionSearchableFactory;

    private ListWithSearchDialogItemAdapter<?> adapter;
    private Cursor cursor;

    private LoadingDelegate loadingDelegate;

    private String searchText;

    private OnItemSelectedListener onItemSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        onItemSelectedListener = (OnItemSelectedListener) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        selectionSearchableFactory = (ListWithSearchDialog.Factory<?, ?>) args
                .getSerializable(SELECTION_SEARCHABLE_FACTORY_ARG);
        itemTypeIsParcelable = args.getBoolean(SELECTION_SEARCHABLE_FACTORY_IS_PARCELABLE_ARG);
        loadingDelegate = LoadingDelegate.create(getContext(), true);
        loadingDelegate.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (itemTypeIsParcelable) {
                selectedItem = savedInstanceState.getParcelable(SELECTED_ITEM_KEY);
            } else {
                selectedItem = savedInstanceState.getSerializable(SELECTED_ITEM_KEY);
            }
            parameterTypeIsParcelable = savedInstanceState.getBoolean(PARAMETER_IS_PARCELABLE_KEY);
            if (parameterTypeIsParcelable) {
                parameter = savedInstanceState.getParcelable(PARAMETER_KEY);
            } else {
                parameter = savedInstanceState.getSerializable(PARAMETER_KEY);
            }
            selectedPosition = savedInstanceState.getInt(SELECTED_POSITION_KEY);
            searchText = savedInstanceState.getString(SEARCH_TEXT_KEY);
            restoreAdapter = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mcm_list_with_search_fragment, container, false);
        RecyclerView listView = (RecyclerView) v
                .findViewById(R.id.mcm_list_with_search_dialog_list);

        adapter = selectionSearchableFactory.createAdapter(getContext());
        listView.setAdapter(adapter);
        adapter.setOnSelectionChangedListener(this);

        SearchView searchView = (SearchView) v
                .findViewById(R.id.mcm_list_with_search_dialog_search);
        //searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        return loadingDelegate.wrapView(v, listView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasParameter()) {
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                    onParameterSelectedBroadcastReceiver,
                    new IntentFilter(ON_PARAMETER_SELECTED_ACTION));
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
        if (parameterTypeIsParcelable) {
            outState.putParcelable(PARAMETER_KEY, (Parcelable) parameter);
        } else {
            outState.putSerializable(PARAMETER_KEY, (Serializable) parameter);
        }
        outState.putBoolean(PARAMETER_IS_PARCELABLE_KEY, parameterTypeIsParcelable);
        outState.putInt(SELECTED_POSITION_KEY, selectedPosition);
        outState.putString(SEARCH_TEXT_KEY, searchText);
        loadingDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (hasParameter()) {
            LocalBroadcastManager.getInstance(getContext())
                    .unregisterReceiver(onParameterSelectedBroadcastReceiver);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        loadingDelegate.show();
        if (hasParameter()) {
            ListWithSearchDialog.FactoryWithParameter factoryWithParameter =
                    (ListWithSearchDialog.FactoryWithParameter) selectionSearchableFactory;
            return factoryWithParameter.createLoader(getContext(), searchText, parameter);
        } else {
            return selectionSearchableFactory.createLoader(getContext(), searchText);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        adapter.swapCursor(data);
        if (restoreAdapter) {
            adapter.toggleSelected(selectedPosition);
            restoreAdapter = false;
        }
        updateLoading();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        restoreAdapter = false;
        selectedItem = null;
        cursor = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        reload(query);
        GenericUtils.hideSoftKeyboard(getContext(), getView(), true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        reload(newText);
        return true;
    }

    private void reload(String query) {
        if (!GenericUtils.equals(query, searchText)) {
            searchText = query;
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onSelectionChanged(int position) {
        this.selectedPosition = position;
        updateSelectedItem();
    }

    private void updateSelectedItem() {
        if (selectedPosition == RecyclerView.NO_POSITION) {
            this.selectedItem = null;
        } else {
            this.cursor.moveToPosition(selectedPosition);
            this.selectedItem = selectionSearchableFactory.createItem(cursor);
        }
        onItemSelectedListener.onItemSelected(selectedItem);
        dismiss();
    }

    private void updateLoading() {
        int count = adapter.getItemCount();
        loadingDelegate.hide(count == 0);
    }

    @Override
    public boolean onClose() {
        return true;
    }

    private boolean hasParameter() {
        return selectionSearchableFactory.hasParameter();
    }

    @Override
    public boolean takeAction(int dialogType, int dialogSubType, Object value) {
        return false;
    }
}
