package com.github.mutoxu_n.randomgroupingapp

import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [Player::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun playerDAO(): PlayerDAO

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(): Database {
            return INSTANCE ?: synchronized(this) {
                // インスタンスが無かったら生成して返す
                val instance = Room.databaseBuilder(
                    App.getContext(),
                    Database::class.java,
                    "db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}