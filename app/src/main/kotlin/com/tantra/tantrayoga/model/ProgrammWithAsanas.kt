package com.tantra.tantrayoga.model

import androidx.databinding.adapters.Converters
import androidx.room.Embedded
import androidx.room.Relation
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
