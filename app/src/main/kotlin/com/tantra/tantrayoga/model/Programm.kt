package com.tantra.tantrayoga.model

import androidx.databinding.adapters.Converters
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.tantra.tantrayoga.model.database.LiveAsanasConverter

@Entity(indices = arrayOf(Index(value = ["UUID"], name = "indexUUID", unique = true)))
data class Programm(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var userUUID: String = "andter",
    var schoolUUID: String = "",
    var UUID: String = "",
    var name: String = "",
    var desc: String = "",
    var numOfCycles: Int = 0,
    var tags: String="",
    var photoUrl: String = "",
    @SerializedName("asanas")
    @Ignore
    var asanas: List<LiveAsana> = ArrayList()
) {

    fun isNew() = id == 0L
}
