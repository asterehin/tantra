package com.tantra.tantrayoga.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = ["UUID"], name = "asanasIndexUUID",  unique = true)))
data class Asana(
    @PrimaryKey val UUID: String,
    val name: String,
    val desc: String,
    val technics: String,
    val effects: String,
    val consciousness: String,
    val audio: String,
    val photo: String,
    val tags: String,
    val sanscritName: String
)
