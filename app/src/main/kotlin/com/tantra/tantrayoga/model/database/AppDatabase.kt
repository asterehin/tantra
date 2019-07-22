package com.tantra.tantrayoga.model.database
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.util.Log
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.PostDao

@Database(entities = [Post::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun appDatabaseInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder<AppDatabase>(context.applicationContext,
                    AppDatabase::class.java, "tantra_db")
                    .fallbackToDestructiveMigration()
                    .setJournalMode(JournalMode.TRUNCATE)
                    .build()
            }

            return INSTANCE!!
        }

        fun destroyInstance() {
            if (INSTANCE?.isOpen() == true) INSTANCE?.close();
            INSTANCE = null
        }
    }
    abstract fun postDao(): PostDao
}