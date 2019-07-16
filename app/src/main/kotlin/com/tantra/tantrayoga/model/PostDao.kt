package com.tantra.tantrayoga.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tantra.tantrayoga.model.Post

@Dao
interface PostDao {
    @get:Query("SELECT * FROM post")
    val all: List<Post>

    @Insert
    fun insertAll(vararg post: Post)

    @Insert
    fun insert( post: Post)

    @Query("SELECT * FROM post where id = :id")
    fun getPost(id: Long): Post
}