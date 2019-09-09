package com.tantra.tantrayoga.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.Asana

@Dao
interface AsanaDao {
    @get:Query("SELECT * FROM asana")
    val all: List<Asana>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asana: Asana)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( asana: Asana): Long

    @Query("SELECT * FROM asana where UUID = :uuid")
    fun getAsana(uuid: String): Asana
}