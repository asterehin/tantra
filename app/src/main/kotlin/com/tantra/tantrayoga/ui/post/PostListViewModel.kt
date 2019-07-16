package com.tantra.tantrayoga.ui.post

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.tantra.tantrayoga.model.Post
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.tantra.tantrayoga.base.BaseViewModel
import com.tantra.tantrayoga.model.PostDao
import com.tantra.tantrayoga.network.PostApi
import javax.inject.Inject
import android.support.design.widget.Snackbar
import com.tantra.tantrayoga.repository.PostRepositoryInterface

class PostListViewModel(private val postDao: PostDao): BaseViewModel(){
    @Inject
    lateinit var postApi: PostApi
//    @Inject
//    lateinit var postRepoInterface: PostRepositoryInterface
//
//    @Inject //@Inject — base annotation whereby the “dependency is requested”
//    fun PostListViewModel(postRepoInterface: PostRepositoryInterface) {
//        this.postRepoInterface = postRepoInterface
//    }

    val postListAdapter: PostListAdapter = PostListAdapter()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage:MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }
    private lateinit var subscription: Disposable

    init{
        loadPosts()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun loadPosts(){
        subscription = Observable.fromCallable { postDao.all }
                .concatMap {
                    dbPostList ->
                        if(dbPostList.isEmpty())
                            postApi.getPosts().concatMap {
                                            apiPostList -> postDao.insertAll(*apiPostList.toTypedArray())
                                 Observable.just(apiPostList)
                                       }
                        else
                            Observable.just(dbPostList)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                        { result -> onRetrievePostListSuccess(result) },
                        { onRetrievePostListError() }
                )
    }

    private fun onRetrievePostListStart(){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(postList:List<Post>){
        postListAdapter.updatePostList(postList)
    }

    private fun onRetrievePostListError(){
        errorMessage.value = com.tantra.tantrayoga.R.string.post_error
    }

    fun onClick(view: View) {
        //https://medium.com/@kyle.dahlin0/room-persistence-library-with-coroutines-cdd32f9fe669
        Snackbar.make(view, "onCLick has been processed", Snackbar.LENGTH_SHORT).show()
//        postRepoInterface.insertAppEntry(Post(0, 0, "title", "body"))
    }
}