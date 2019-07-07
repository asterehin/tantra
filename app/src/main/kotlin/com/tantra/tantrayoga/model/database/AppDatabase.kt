package com.tantra.tantrayoga.model.database
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.PostDao

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}