package com.tantra.tantrayoga.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    indices = arrayOf(
        Index(
            value = ["programmUUID", "asanaUUID", "sequence"],
            name = "liveAsanasIndexUUID",
            unique = true
        )
    ),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Programm::class,
            parentColumns = arrayOf("UUID"),
            childColumns = arrayOf("programmUUID"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class LiveAsana(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val programmUUID: String,
    val asanaUUID: String,
    val sequence: Int,
    val playAudio: Boolean,
    val dueTime: Int, //in sec
    val consciousnessTime: Int, //in sec
    val showTimer: Boolean,
    val playSignals: Boolean
)
