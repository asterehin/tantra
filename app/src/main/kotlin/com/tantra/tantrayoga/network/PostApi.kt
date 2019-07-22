package com.tantra.tantrayoga.network

import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.PostResponse
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 */
interface PostApi {
    /**
     * Get the list of the pots from the API
     */
//    @GET("/asterehin/tantra/posts/")
//    fun getPosts(): Observable<List<Post>>

    @GET("/asterehin/tantra/posts/")
    fun getPosts(): Deferred<Response<List<Post>>>
}