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
        ),
        ForeignKey(
            entity = Asana::class,
            parentColumns = arrayOf("UUID"),
            childColumns = arrayOf("asanaUUID"),
            onDelete = ForeignKey.NO_ACTION
        )
    )
)
data class LiveAsana(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var programmUUID: String = "",
    var asanaUUID: String = "",
    var sequence: Int = 0,
    var playAudio: Boolean = true,
    var dueTime: Int = 0, //in sec
    var consciousnessTime: Int = 0, //in sec
    var showTimer: Boolean = false,
    var playSignals: Boolean = false
)
