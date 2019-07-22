package com.tantra.tantrayoga.model

import android.arch.persistence.room.*
import com.tantra.tantrayoga.model.Post

@Dao
interface PostDao {
    @get:Query("SELECT * FROM post")
    val all: List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( post: Post): Long

    @Query("SELECT * FROM post where id = :id")
    fun getPost(id: Long): Post
}