package com.tantra.tantrayoga.ui.asanas

import androidx.lifecycle.MutableLiveData
import com.tantra.tantrayoga.model.Asana

import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.dao.AsanaDao
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AsanasListViewModel(private val asanaDao: AsanaDao) : BaseViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val asanasListAdapter = AsanasListAdapter()
    val popularMoviesLiveData = MutableLiveData<MutableList<Asana>>()


    init {
        loadAsanas()
    }

    private fun loadAsanas() = scope.launch {
        popularMoviesLiveData.postValue(asanaDao.all.toMutableList())
    }

    fun onRetrieveAsanaListSuccess(postList: List<Asana>) {
        asanasListAdapter.updateAsanaList(postList)
    }

    fun updateList(postList: MutableList<Asana>?) {
        onRetrieveAsanaListSuccess(postList?.toList() ?: emptyList())

    }
}
