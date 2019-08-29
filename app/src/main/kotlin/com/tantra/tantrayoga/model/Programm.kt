package com.tantra.tantrayoga.model

import android.arch.persistence.room.*
import android.databinding.adapters.Converters
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.tantra.tantrayoga.model.database.LiveAsanasConverter

@Entity(indices = arrayOf(Index(value = ["UUID"], name = "indexUUID", unique = true)))
data class Programm(
    @PrimaryKey(autoGenerate = true) var id: Long =0L,
    var userUUID: String="andter",
    var schoolUUID: String="",
    var UUID: String="",
    var name: String="",
    var desc: String="",
    @SerializedName("asanas")
    @Ignore
    var asanas: List<LiveAsana> = ArrayList()
)
