package com.tantra.tantrayoga.network

import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.PostResponse
import com.tantra.tantrayoga.model.Programm
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 */
interface ProgrammApi {
    /**
     * Get the list of the programms from the API
     */

    @GET("/asterehin/tantra/programms/")
    fun getProgramms(): Deferred<Response<List<Programm>>>
}