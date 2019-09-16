package com.tantra.tantrayoga.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tantra.tantrayoga.model.Asana
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.Programm
import com.tantra.tantrayoga.model.dao.AsanaDao
import com.tantra.tantrayoga.model.dao.LiveAsanaDao
import com.tantra.tantrayoga.model.dao.ProgrammDao

@Database(entities = [ Programm::class, Asana::class, LiveAsana::class], version = 21)
//@TypeConverters(LiveAsanasConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun appDatabaseInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    AppDatabase::class.java, "tantra_db"
                )
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

    abstract fun programmDao(): ProgrammDao
    abstract fun asanaDao(): AsanaDao
    abstract fun liveAsanaDao(): LiveAsanaDao
}