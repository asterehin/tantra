package com.tantra.tantrayoga.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.provider.MediaStore
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.PostDao
import com.tantra.tantrayoga.network.PostApi
import kotlinx.coroutines.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import javax.annotation.Resource
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

//@Inject
class PostRepository() : PostRepositoryInterface, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var mDao: PostDao

    @Inject
    constructor(productDao: PostDao) : this() {
        this.mDao = productDao
    }

    override suspend fun getAppEntry(id: Long): Post? {
        return coroutineScope {
            async {
                mDao.getPost(id)
            }.await()
        }
    }

    override suspend fun getAll(): List<Post> {
        return coroutineScope {
            async {
                mDao.all
            }.await()
        }
    }

//    suspend fun getAllAppEntries(): List<Post> {
//        return async {
//            mDao.getAllAppEntries()
//        }.await()
//    }
//

    override fun insertAppEntry(post: Post): Job {
        return launch {
            mDao.insert(post)
        }
    }

    //
//    fun deleteAppEntryForPackageName(packageName: String): Job {
//        return launch {
//            mDao.deleteForPackageName(packageName)
//        }
//    }

}