package com.tantra.tantrayoga.common.dependencyinjection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import com.tantra.tantrayoga.model.database.AppDatabase
import com.tantra.tantrayoga.model.database.AppDatabase.Companion.appDatabaseInstance
import com.tantra.tantrayoga.ui.post.PostListViewModel

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            val db = //Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "posts").build()
            appDatabaseInstance(activity.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return PostListViewModel(db.postDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}