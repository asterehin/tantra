package com.tantra.tantrayoga.model.dao

import com.tantra.tantrayoga.model.Programm
import com.tantra.tantrayoga.model.ProgrammWithAsanas
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Transaction
    fun delete(programmWithAsanas: ProgrammWithAsanas) {
        programmWithAsanas.liveAsanas.forEach {
            deleteLiveAsanas(it)
        }
        deleteProgramm(programmWithAsanas.programm)
    }

    @Delete
    fun deleteProgramm(programm: Programm): Int

    @Delete
    fun deleteLiveAsanas(liveAsana: LiveAsana): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(liveAsana: LiveAsana): Long
}