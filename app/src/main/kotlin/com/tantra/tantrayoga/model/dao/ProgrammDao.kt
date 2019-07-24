package com.tantra.tantrayoga.model.dao

import android.arch.persistence.room.*
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.Programm

@Dao
interface ProgrammDao {
    @get:Query("SELECT * FROM programm")
    val all: List<Programm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg programm: Programm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( programm: Programm): Long

    @Query("SELECT * FROM programm where UUID = :uuid")
    fun getProgramm(uuid: String): Programm
}