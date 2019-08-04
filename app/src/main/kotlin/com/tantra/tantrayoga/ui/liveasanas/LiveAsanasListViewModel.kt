package com.tantra.tantrayoga.ui.liveasanas

import android.arch.lifecycle.*
import com.tantra.tantrayoga.model.LiveAsana

import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.dao.LiveAsanaDao
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LiveAsanasListViewModel(private val liveLiveAsanaDao: LiveAsanaDao) : BaseViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val liveAsanasListAdapter = LiveAsanasListAdapter()
    val popularMoviesLiveData = MutableLiveData<MutableList<LiveAsana>>()


    init {
        loadLiveAsanas()
    }

    private fun loadLiveAsanas() = scope.launch {
        popularMoviesLiveData.postValue(liveLiveAsanaDao.all.toMutableList())
    }

    fun onRetrieveLiveAsanaListSuccess(postList: List<LiveAsana>) {
        liveAsanasListAdapter.updateLiveAsanaList(postList)
    }

    fun updateList(postList: MutableList<LiveAsana>?) {
        onRetrieveLiveAsanaListSuccess(postList?.toList() ?: emptyList())

    }
}
