package com.github.mutoxu_n.randomgroupingapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayerDAO {
    @Insert
    fun insert(player: Player): Long

    @Update
    fun update(player: Player)

    @Delete
    fun delete(player: Player)

    @Query("SELECT * FROM players")
    fun getAll(): List<Player>
}