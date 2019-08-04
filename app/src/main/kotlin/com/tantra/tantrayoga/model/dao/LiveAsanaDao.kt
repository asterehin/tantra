package com.tantra.tantrayoga.model.dao

import android.arch.persistence.room.*
import com.tantra.tantrayoga.model.Post
import com.tantra.tantrayoga.model.Asana
import com.tantra.tantrayoga.model.LiveAsana
import com.tantra.tantrayoga.model.LiveAsanaDetails

@Dao
interface LiveAsanaDao {
    @get:Query("SELECT * FROM liveAsana")
    val all: List<LiveAsana>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asana: LiveAsana)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asana: LiveAsana): Long

    @Query("SELECT * FROM liveAsana where programmUUID = :programmUUID")
    fun getLiveAsana(programmUUID: String): List<LiveAsana>

    @Query("SELECT asana.*,liveAsana.* FROM liveAsana  JOIN asana ON liveAsana.asanaUUID = asana.uuid WHERE programmUUID = :uuid ")
    fun getLiveAsanaDetailsByUuid(uuid: String): MutableList<LiveAsanaDetails>
}