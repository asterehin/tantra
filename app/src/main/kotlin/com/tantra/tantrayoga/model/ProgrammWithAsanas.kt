package com.tantra.tantrayoga.model

import android.arch.persistence.room.*
import android.databinding.adapters.Converters
import com.squareup.moshi.Json
import com.tantra.tantrayoga.model.database.LiveAsanasConverter

//https://habr.com/ru/post/349280/
class ProgrammWithAsanas() {
    @Embedded
    lateinit var programm: Programm
    @Relation(
        parentColumn = "UUID",
        entityColumn = "programmUUID",
        entity = LiveAsana::class
    )
    lateinit var liveAsanas: List<LiveAsana>
    fun isPersonal() = programm.schoolUUID.isEmpty()

}
