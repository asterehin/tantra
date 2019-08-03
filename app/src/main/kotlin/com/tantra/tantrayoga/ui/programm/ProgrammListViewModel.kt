package com.tantra.tantrayoga.ui.programm

import android.arch.lifecycle.*
import android.view.View
import com.tantra.tantrayoga.model.Programm

import com.tantra.tantrayoga.base.BaseViewModel
import javax.inject.Inject
import android.support.design.widget.Snackbar
import android.util.Log
import com.tantra.tantrayoga.model.Event
import com.tantra.tantrayoga.model.Programm2
import com.tantra.tantrayoga.model.dao.AsanaDao
import com.tantra.tantrayoga.model.dao.ProgrammDao
import com.tantra.tantrayoga.network.ProgrammApi
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.coroutines.CoroutineContext

class ProgrammListViewModel(private val programmDao: ProgrammDao, private val asanaDao: AsanaDao) : BaseViewModel() {

    @Inject
    lateinit var programmApi: ProgrammApi
    //    @Inject
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val programmListAdapter = ProgrammListAdapter()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val programmsLiveData = MutableLiveData<MutableList<Programm>>()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadProgramms() }

    private val _navigateToDetails = MutableLiveData<Event<String>>()
    val navigateToDetails: LiveData<Event<String>>
        get() = _navigateToDetails

    init {
        loadProgramms()
    }


    private fun loadProgramms() {

        onRetrieveProgrammListStart()

        scope.launch {

//            programmApi.getProgramms(object : Callback<List<Programm>> {
//                override fun onFailure(call: Call<List<Programm>>, t: Throwable) {
//                    Log.e("ProgrammListViewModel-onFailure 61 ", "oopssss") //todo No Retrofit annotation found.
//                }
//
//                override fun onResponse(call: Call<List<Programm>>, response: Response<List<Programm>>) {
//                    Log.e("ProgrammListViewModel-onResponse 65 ", "response ")
//                }
//
//
//            })

            val call = programmApi.getProgrammsCall()
            call.enqueue(object : Callback<List<Programm2>> {
                override fun onResponse(call: Call<List<Programm2>>, response: Response<List<Programm2>>) {
                    Log.e("ProgrammListViewModel-onResponse 78 ", "-" + response.body()?.size)
                }

                override fun onFailure(call: Call<List<Programm2>>, t: Throwable) {

                    Log.d("Error", t.message)
                }
            })

            val programmsRequest = programmApi.getProgramms()
            try {
                val response = programmsRequest.await()
                if (response.isSuccessful) {
                    val programms = response.body()
                    programmDao.insertAll(*programms!!.toTypedArray())
                    programmsLiveData.postValue(programmDao.all.toMutableList())

                } else {
                    onRetrieveProgrammListError()
                    Log.d("MainActivity ", response.errorBody().toString())
                }

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

    fun onRetrieveProgrammListSuccess(postList: List<Programm>) {
        programmListAdapter.updateProgrammList(postList)
    }

    private fun onSomeException(e: Exception) {
        errorMessage.postValue(com.tantra.tantrayoga.R.string.some_error)
    }

    private fun onRetrieveProgrammListError() {
        errorMessage.value = com.tantra.tantrayoga.R.string.post_error
    }

    fun onClick(view: View) {
        //https://medium.com/@kyle.dahlin0/room-persistence-library-with-coroutines-cdd32f9fe669
        Snackbar.make(view, "onCLick has been processed", Snackbar.LENGTH_SHORT).show()
        _navigateToDetails.value = Event("some content")  // Trigger the event by setting a new Event as a new value

    }

    fun addNewItem(name: String, desc: String) {
        scope.launch {
            val i = programmDao.insert(Programm(0, "andter", UUID.randomUUID().toString(), name, desc))
            programmsLiveData.postValue(programmDao.all.toMutableList())
        }
    }

    fun updateList(postList: MutableList<Programm>?) {
        onRetrieveProgrammListSuccess(postList?.toList() ?: emptyList())

        onRetrieveProgrammListFinish()
    }
}
