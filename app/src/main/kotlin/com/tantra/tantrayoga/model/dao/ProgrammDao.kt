package com.tantra.tantrayoga.model.dao

import android.arch.persistence.room.*
import com.tantra.tantrayoga.model.Programm
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import android.arch.persistence.room.Transaction
import android.arch.persistence.room.OnConflictStrategy
import com.tantra.tantrayoga.model.LiveAsana


@Dao
interface ProgrammDao {
    @get:Query("SELECT * FROM programm")
    val all: List<Programm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg programm: Programm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(programm: Programm): Long

    @Query("SELECT * FROM programm where UUID = :uuid")
    fun getProgramm(uuid: String): Programm

    @Transaction  //для обеспечения целостности, т.к. может такое быть, что данные не полностью загружены
    @Query("SELECT * FROM programm WHERE UUID = :uuid")
    fun loadProgrammWithAsanasByUuid(uuid: String): LiveData<ProgrammWithAsanas>

    @Transaction  //для обеспечения целостности, т.к. может такое быть, что данные не полностью загружены
    @Query("SELECT * FROM programm ")
    fun loadAllProgrammWithAsanas(): MutableList<ProgrammWithAsanas>

    @Transaction
    @Query("SELECT * FROM programm WHERE UUID = :uuid")
    fun getProgrammWithAsanasByUuid(uuid: String): ProgrammWithAsanas

    @Transaction
    fun insert(programmWithAsanas: ProgrammWithAsanas) {
        insert(programmWithAsanas.programm)
        programmWithAsanas.liveAsanas.forEach {
                insert(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(tag: LiveAsana): Long
}