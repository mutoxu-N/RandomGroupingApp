package com.github.mutoxu_n.randomgroupingapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = false) val name: String,
    @ColumnInfo(name = "point") val point: Int
)