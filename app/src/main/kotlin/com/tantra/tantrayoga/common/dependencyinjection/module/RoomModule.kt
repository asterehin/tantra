package com.tantra.tantrayoga.common.dependencyinjection.module

import android.app.Application
import com.tantra.tantrayoga.common.dependencyinjection.application.ApplicationScope
import com.tantra.tantrayoga.common.dependencyinjection.logging.MyLogger
import com.tantra.tantrayoga.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import com.tantra.tantrayoga.network.PostApi
import com.tantra.tantrayoga.repository.PostRepositoryInterface
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import android.arch.persistence.room.Room
import com.tantra.tantrayoga.model.PostDao
import com.tantra.tantrayoga.model.database.AppDatabase
import com.tantra.tantrayoga.model.database.AppDatabase.Companion.appDatabaseInstance
import com.tantra.tantrayoga.repository.PostRepository


//https://medium.com/@marco_cattaneo/integrate-dagger-2-with-room-persistence-library-in-few-lines-abf48328eaeb

/**
 * Module which provides all required dependencies about network
 */
@Module //@Module — classes which methods “provide dependencies”
class RoomModule(mApplication: Application, var demoDatabase: AppDatabase = appDatabaseInstance(mApplication)) {
//    private var demoDatabase: AppDatabase

//    init {
//        demoDatabase = Room.databaseBuilder(mApplication, AppDatabase::class.java, "app-db").build()
//    }

    @Singleton
    @Provides //@Provide — methods inside @Module, which “tell Dagger how we want to build and present a dependency“
    fun providesAppDatabase(): AppDatabase {
        return demoDatabase
    }

    @Singleton
    @Provides
    fun providesPostDao(demoDatabase: AppDatabase): PostDao {
        return demoDatabase.postDao()
    }

    @Singleton
    @Provides
    fun postRepository(productDao: PostDao): PostRepositoryInterface {
        return PostRepository(productDao)
    }

}