package com.tantra.tantrayoga.model

import android.database.Cursor
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(indices = arrayOf(Index(value = ["UUID"], name = "asanasIndexUUID",  unique = true)))
data class Asana(
    @PrimaryKey val UUID: String = "",
    val name: String = "",
    val desc: String = "",
    val technics: String = "",
    val effects: String = "",
    val consciousness: String = "",
    val audio: String = "",
    val photo: String = "",
    val tags: String = "",
    val sanscritName: String = ""
): Serializable {

    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndex("UUID")),
        cursor.getString(cursor.getColumnIndex("name")),
        cursor.getString(cursor.getColumnIndex("desc")),
        cursor.getString(cursor.getColumnIndex("technics")),
        cursor.getString(cursor.getColumnIndex("effects")),
        cursor.getString(cursor.getColumnIndex("consciousness")),
        cursor.getString(cursor.getColumnIndex("audio")),
        cursor.getString(cursor.getColumnIndex("photo")),
        cursor.getString(cursor.getColumnIndex("tags")),
        cursor.getString(cursor.getColumnIndex("sanscritName"))
    )
}
