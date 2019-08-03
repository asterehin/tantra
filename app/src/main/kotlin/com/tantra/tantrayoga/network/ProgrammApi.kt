package com.tantra.tantrayoga.network

import com.tantra.tantrayoga.model.*
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The interface which provides methods to get result of webservices
 */
interface ProgrammApi {
    /**
     * Get the list of the programms from the API
     */

    @GET("/asterehin/tantra/programms/")
    fun getProgramms(): Deferred<Response<List<Programm>>>

    @GET("/asterehin/tantra/asanas/")
    fun getAsanas(): Deferred<Response<List<Asana>>>

//    @GET("/asterehin/tantra/programms/") //todo this approach is also working
//    fun getProgrammsCall(): Call<List<Programm2>>
}