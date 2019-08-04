package com.tantra.tantrayoga.model

import android.arch.persistence.room.*
import android.databinding.adapters.Converters
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


}
