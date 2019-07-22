package com.tantra.tantrayoga.ui.post

import android.arch.lifecycle.*
import android.view.View
import com.tantra.tantrayoga.model.Post
import io.reactivex.disposables.Disposable

import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.PostDao
import com.tantra.tantrayoga.network.PostApi
import javax.inject.Inject
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.tantra.tantrayoga.model.Event
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class PostListViewModel(private val postDao: PostDao) : BaseViewModel() {

    @Inject
    lateinit var postApi: PostApi
    //    @Inject
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private lateinit var lifecycleOwner: LifecycleOwner


    val postListAdapter: PostListAdapter = PostListAdapter()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val popularMoviesLiveData = MutableLiveData<MutableList<Post>>()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }

    private val _navigateToDetails = MutableLiveData<Event<String>>()
    val navigateToDetails: LiveData<Event<String>>
        get() = _navigateToDetails

    private lateinit var subscription: Disposable

    init {
        loadPosts()
    }

    // Assign our LifecyclerObserver to LifecycleOwner
    fun addLocationUpdates(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun loadPosts() {

        onRetrievePostListStart()

        scope.launch {
            val postRequest = postApi.getPosts()
            try {
                val response = postRequest.await()
                if (response.isSuccessful) {
                    val posts = response.body()
                    postDao.insertAll(*posts!!.toTypedArray())
                    popularMoviesLiveData.postValue(postDao.all.toMutableList())

                } else {
                    onRetrievePostListError()
                    Log.d("MainActivity ", response.errorBody().toString())
                }

            } catch (e: Exception) {
                onSomeException(e)
            }
        }

    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    fun onRetrievePostListSuccess(postList: List<Post>) {
        postListAdapter.updatePostList(postList)
    }

    private fun onSomeException(e: Exception) {
        errorMessage.postValue(com.tantra.tantrayoga.R.string.some_error)
    }

    private fun onRetrievePostListError() {
        errorMessage.value = com.tantra.tantrayoga.R.string.post_error
    }

    fun onClick(view: View) {
        //https://medium.com/@kyle.dahlin0/room-persistence-library-with-coroutines-cdd32f9fe669
        Snackbar.make(view, "onCLick has been processed", Snackbar.LENGTH_SHORT).show()
        _navigateToDetails.value = Event("some content")  // Trigger the event by setting a new Event as a new value

    }

    fun addNewItem(name: String) {
        scope.launch {
            val i = postDao.insert(Post(5, 0, "title", name))
            popularMoviesLiveData.postValue(postDao.all.toMutableList())
        }
    }

    fun updateList(postList: MutableList<Post>?) {
        onRetrievePostListSuccess(postList?.toList() ?: emptyList())

        onRetrievePostListFinish()
    }
}
