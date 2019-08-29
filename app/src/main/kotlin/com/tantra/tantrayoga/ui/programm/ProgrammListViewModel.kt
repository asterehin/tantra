package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.*
import android.view.View
import com.tantra.tantrayoga.model.Programm

import com.tantra.tantrayoga.base.BaseViewModel
import javax.inject.Inject
import android.support.design.widget.Snackbar
import android.util.Log
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import com.tantra.tantrayoga.model.dao.AsanaDao
import com.tantra.tantrayoga.model.dao.ProgrammDao
import com.tantra.tantrayoga.network.ProgrammApi
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class ProgrammListViewModel(
    private val programmDao: ProgrammDao,
    private val asanaDao: AsanaDao
) : BaseViewModel() {

    @Inject
    lateinit var programmApi: ProgrammApi
    //    @Inject
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val programmListAdapter = ProgrammListAdapter()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val programmsWithAsanasLiveData = MutableLiveData<MutableList<ProgrammWithAsanas>>()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadProgramms() }

    private val openAddProgrammDialog = MutableLiveData<Event<String>>()
    val tapOnAddFab: LiveData<Event<String>>
        get() = openAddProgrammDialog

    //    var selectedProgramm = MutableLiveData<ProgrammWithAsanas>()
    var onProgrammActionEvent = MutableLiveData<Event<ProgrammWithAsanas>>()

    init {
        loadProgramms()
        programmListAdapter.onProgrammActionEvent = onProgrammActionEvent
    }


    private fun loadProgramms() {

        onRetrieveProgrammListStart()

        scope.launch {

            //            val call = programmApi.getProgrammsCall() //todo this approach is also working
//            call.enqueue(object : Callback<List<Programm2>> {
//                override fun onResponse(call: Call<List<Programm2>>, response: Response<List<Programm2>>) {
//                    Log.e("ProgrammListViewModel-onResponse 78 ", "-" + response.body()?.size)
//                }
//
//                override fun onFailure(call: Call<List<Programm2>>, t: Throwable) {
//
//                    Log.d("Error", t.message)
//                }
//            })

            val programmsRequest = programmApi.getProgramms()
            try {
                val response = programmsRequest.await()
                if (response.isSuccessful) {
                    val programms = response.body()
                    programms!!.forEach { programm ->
                        programm.asanas.forEach { liveAsana ->
                            liveAsana.programmUUID = programm.UUID
                        }
                        val programmWithAsanas = ProgrammWithAsanas()
                        programmWithAsanas.programm = programm
                        programmWithAsanas.liveAsanas = programm.asanas
                        programmDao.insert(programmWithAsanas)
                    }

                } else {
                    onRetrieveProgrammListError()
                    Log.d("MainActivity ", response.errorBody().toString())
                }

                programmsWithAsanasLiveData.postValue(programmDao.loadAllProgrammWithAsanas())

            } catch (e: Exception) {
                onSomeException(e)
            }
        }
        scope.launch {
            val asanasRequest = programmApi.getAsanas()
            try {
                val response = asanasRequest.await()
                if (response.isSuccessful) {
                    val asanasList = response.body()
                    asanaDao.insertAll(*asanasList!!.toTypedArray())
                } else {
                    onRetrieveProgrammListError()
                    Log.d("MainActivity ", response.errorBody().toString())
                }

            } catch (e: Exception) {
                onSomeException(e)
            }
        }

    }

    private fun onRetrieveProgrammListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveProgrammListFinish() {
        loadingVisibility.value = View.GONE
    }

    fun onRetrieveProgrammListSuccess(mutableList: MutableList<ProgrammWithAsanas>) {
        programmListAdapter.updateProgrammList(mutableList)
    }

    private fun onSomeException(e: Exception) {
        errorMessage.postValue(com.tantra.tantrayoga.R.string.some_error)
    }

    private fun onRetrieveProgrammListError() {
        loadingVisibility.value = View.GONE

        errorMessage.value = com.tantra.tantrayoga.R.string.post_error
    }

    fun onClick(view: View) {
        //https://medium.com/@kyle.dahlin0/room-persistence-library-with-coroutines-cdd32f9fe669
        Snackbar.make(view, "onCLick has been processed", Snackbar.LENGTH_SHORT).show()
        openAddProgrammDialog.value =
            Event("some content"/*, Event.Action.NONE*/)  // Trigger the event by setting a new Event as a new value

    }

    fun saveProgramm(programm: Programm) {
        scope.launch {
            programmDao.insert(programm)
            programmsWithAsanasLiveData.postValue(programmDao.loadAllProgrammWithAsanas())
        }
    }

    fun updateList(mutableList: MutableList<ProgrammWithAsanas>) {
        onRetrieveProgrammListSuccess(mutableList)

        onRetrieveProgrammListFinish()
    }

    fun deleteProgramm(programmWithAsanas: ProgrammWithAsanas) {
        scope.launch {
            programmDao.delete(programmWithAsanas)
            programmsWithAsanasLiveData.postValue(programmDao.loadAllProgrammWithAsanas())
        }
    }
}
