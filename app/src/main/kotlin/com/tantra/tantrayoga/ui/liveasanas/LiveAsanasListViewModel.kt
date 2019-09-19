package com.tantra.tantrayoga.ui.liveasanas

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar

import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.*
import com.tantra.tantrayoga.model.dao.LiveAsanaDao
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LiveAsanasListViewModel(
    private val liveAsanaDao: LiveAsanaDao,
    val uuid: String
) : BaseViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val liveAsanasListAdapter = LiveAsanasListAdapter()
    val liveAsanasListLiveData = MutableLiveData<MutableList<LiveAsanaDetails>>()
    var onItemActionEvent = MutableLiveData<Event<LiveAsanaDetails>>()

    private val openAdditemDialog = MutableLiveData<Event<String>>()
    val tapOnAddFab: LiveData<Event<String>>
        get() = openAdditemDialog

    init {
        loadLiveAsanas()
        liveAsanasListAdapter.onItemActionEvent = onItemActionEvent
    }

    private fun loadLiveAsanas() = scope.launch {
        liveAsanasListLiveData.postValue(liveAsanaDao.getLiveAsanaDetailsByUuid(uuid))
    }

    fun onRetrieveLiveAsanaListSuccess(liveAsanasDetailsList: List<LiveAsanaDetails>) {
        liveAsanasListAdapter.updateLiveAsanaList(liveAsanasDetailsList)
    }

    fun updateList(list: MutableList<LiveAsanaDetails>?) {
        onRetrieveLiveAsanaListSuccess(list?.toList() ?: emptyList())

    }

    fun deleteLiveAsana(liveAsana: LiveAsana) {
        scope.launch {
            liveAsanaDao.deleteLiveAsana(liveAsana)
            liveAsanasListLiveData.postValue(liveAsanaDao.getLiveAsanaDetailsByUuid(uuid))
        }
    }

    fun onClick(view: View) {
        //https://medium.com/@kyle.dahlin0/room-persistence-library-with-coroutines-cdd32f9fe669
        Snackbar.make(view, "onCLick has been processed", Snackbar.LENGTH_SHORT).show()
        openAdditemDialog.value = Event(uuid)  // Trigger the event by setting a new Event as a new value
    }

    fun saveLiveAsana(liveAsana: LiveAsana) {
        scope.launch {
            liveAsanaDao.insert(liveAsana)
            liveAsanasListLiveData.postValue(liveAsanaDao.getLiveAsanaDetailsByUuid(uuid))
        }
    }

}
