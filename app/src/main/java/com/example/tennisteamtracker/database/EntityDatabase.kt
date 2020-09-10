package com.example.tennisteamtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 2, entities = [Player::class, GameDay::class, Game::class])
internal abstract class EntityDatabase : RoomDatabase() {
    abstract val playerDatabaseDao: PlayerDatabaseDao
    abstract val gameDatabaseDao: GameDatabaseDao
    abstract val gameDayDatabaseDao: GameDayDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: EntityDatabase? = null
        fun getInstance(context: Context): EntityDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EntityDatabase::class.java,
                        "app_entity_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}