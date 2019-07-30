package com.tantra.tantrayoga.model.database
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tantra.tantrayoga.model.Asana
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.dao.PostDao
import com.tantra.tantrayoga.model.Programm
import com.tantra.tantrayoga.model.dao.AsanaDao
import com.tantra.tantrayoga.model.dao.ProgrammDao

@Database(entities = [Post::class, Programm::class, Asana::class], version = 6)
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
    abstract fun programmDao(): ProgrammDao
    abstract fun asanaDao(): AsanaDao
}