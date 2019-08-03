package com.tantra.tantrayoga.model

import android.arch.persistence.room.*
import android.databinding.adapters.Converters
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.tantra.tantrayoga.model.database.LiveAsanasConverter

data class Programm2(
    val id: Long,
    val userUUID: String,
    val UUID: String,
    val name: String,
    val desc: String,
    @SerializedName("asanas") val asanas: ArrayList<LiveAsana> = ArrayList()
)
