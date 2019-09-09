package com.tantra.tantrayoga.model.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types.newParameterizedType
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tantra.tantrayoga.model.LiveAsana


class LiveAsanasConverter {
    @TypeConverter
    fun fromString(jsonResponse: String): List<LiveAsana> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, LiveAsana::class.java)
        val adapter:JsonAdapter<List<LiveAsana>> = moshi.adapter(type)

        return adapter.fromJson(jsonResponse)?: emptyList()
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}