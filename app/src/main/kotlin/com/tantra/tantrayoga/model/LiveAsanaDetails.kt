package com.tantra.tantrayoga.model

import androidx.databinding.adapters.Converters
import androidx.room.Embedded
import com.squareup.moshi.Json
import com.tantra.tantrayoga.model.database.LiveAsanasConverter

//https://habr.com/ru/post/349280/
class LiveAsanaDetails() {
    @Embedded
    lateinit var asana: Asana
    @Embedded
    lateinit var liveAsana: LiveAsana

    //    @Relation(
//        parentColumn = "UUID",
//        entityColumn = "asanaUUID",
//        entity = LiveAsana::class
//    )
//    lateinit var liveAsana: List<LiveAsana>
    fun isNew() = liveAsana.id == 0L


}
