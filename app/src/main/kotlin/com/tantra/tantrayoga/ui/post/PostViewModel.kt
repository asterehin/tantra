package com.tantra.tantrayoga.ui.post

import android.arch.lifecycle.MutableLiveData
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.base.BaseViewModel

class PostViewModel: BaseViewModel() {
    private val postTitle = MutableLiveData<String>()
    private val postBody = MutableLiveData<String>()

    fun bind(post: Post){
        postTitle.value = post.title
        postBody.value = post.body
    }

    fun getPostTitle():MutableLiveData<String>{
        return postTitle
    }

    fun getPostBody():MutableLiveData<String>{
        return postBody
    }
}