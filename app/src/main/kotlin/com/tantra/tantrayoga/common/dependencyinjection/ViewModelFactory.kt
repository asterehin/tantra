package com.tantra.tantrayoga.common.dependencyinjection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import com.tantra.tantrayoga.model.database.AppDatabase.Companion.appDatabaseInstance
import com.tantra.tantrayoga.ui.asanas.AsanasListViewModel
import com.tantra.tantrayoga.ui.liveasanas.LiveAsanasListViewModel
import com.tantra.tantrayoga.ui.post.PostListViewModel
import com.tantra.tantrayoga.ui.programm.ProgrammListViewModel

class ViewModelFactory(private val activity: AppCompatActivity, private val uuid: String = "") : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            val db = //Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "posts").build()
                appDatabaseInstance(activity.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return PostListViewModel(db.postDao()) as T
        } else if (modelClass.isAssignableFrom(ProgrammListViewModel::class.java)) {
            val db = appDatabaseInstance(activity.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return ProgrammListViewModel(db.programmDao(), db.asanaDao()) as T
        } else if (modelClass.isAssignableFrom(AsanasListViewModel::class.java)) {
            val db = appDatabaseInstance(activity.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return AsanasListViewModel(db.asanaDao()) as T
        } else if (modelClass.isAssignableFrom(LiveAsanasListViewModel::class.java)) {
            val db = appDatabaseInstance(activity.applicationContext)
            @Suppress("UNCHECKED_CAST")
            return LiveAsanasListViewModel(db.liveAsanaDao(), uuid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}