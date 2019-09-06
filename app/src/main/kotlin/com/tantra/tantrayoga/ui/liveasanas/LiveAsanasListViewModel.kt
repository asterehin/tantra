package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.*
import android.support.design.widget.Snackbar
import android.view.View
import com.tantra.tantrayoga.model.LiveAsana

import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.LiveAsanaDetails
import com.tantra.tantrayoga.model.dao.LiveAsanaDao
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LiveAsanasListViewModel(
    private val liveLiveAsanaDao: LiveAsanaDao,
    val uuid: String
) : BaseViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val liveAsanasListAdapter = LiveAsanasListAdapter()
    val liveAsanasList = MutableLiveData<MutableList<LiveAsanaDetails>>()


    init {
        loadLiveAsanas()
    }

    private fun loadLiveAsanas() = scope.launch {
        liveAsanasList.postValue(liveLiveAsanaDao.getLiveAsanaDetailsByUuid(uuid))
    }

    fun onRetrieveLiveAsanaListSuccess(liveAsanasDetailsList: List<LiveAsanaDetails>) {
        liveAsanasListAdapter.updateLiveAsanaList(liveAsanasDetailsList)
    }

    fun updateList(list: MutableList<LiveAsanaDetails>?) {
        onRetrieveLiveAsanaListSuccess(list?.toList() ?: emptyList())

    }

    fun onClick(view: View) {
        //https://medium.com/@kyle.dahlin0/room-persistence-library-with-coroutines-cdd32f9fe669
        Snackbar.make(view, "onCLick has been processed", Snackbar.LENGTH_SHORT).show()

    }
}
