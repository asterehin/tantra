package com.tantra.tantrayoga.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Programm(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val userUUID: String,
    val UUID: String,
    val name: String,
    val desc: String
)
