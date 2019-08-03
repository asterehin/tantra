package com.tantra.tantrayoga.common.dependencyinjection.module

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tantra.tantrayoga.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import com.tantra.tantrayoga.network.PostApi
import com.tantra.tantrayoga.network.ProgrammApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Module which provides all required dependencies about network
 */
@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
object NetworkModule {
    /**
     * Provides the com.tantra.tantrayoga.model.Post service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the com.tantra.tantrayoga.model.Programm service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideProgrammApi(retrofit: Retrofit): ProgrammApi {
        return retrofit.create(ProgrammApi::class.java)
    }

    /**
     * Provides the com.tantra.tantrayoga.model.Post service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the com.tantra.tantrayoga.model.Post service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun providePostApi(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(MoshiConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }
}