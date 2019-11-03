package com.example.tp2.database

import androidx.room.*
import com.example.tp2.model.User

@Dao
interface UserDao
{
    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * from user WHERE id = :key")
    fun get(key: Long): User?

    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    fun getLastUser(): User?
}