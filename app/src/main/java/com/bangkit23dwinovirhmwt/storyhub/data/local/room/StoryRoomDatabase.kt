package com.bangkit23dwinovirhmwt.storyhub.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit23dwinovirhmwt.storyhub.data.local.entity.StoryEntity

@Database(entities = [StoryEntity::class], version = 1, exportSchema = false)
abstract class StoryRoomDatabase : RoomDatabase() {
    abstract fun storyHubDao(): StoryHubDao

    companion object {
        @Volatile
        private var instance: StoryRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryRoomDatabase {
            if (instance == null) {
                synchronized(StoryRoomDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StoryRoomDatabase::class.java, "story_hub_database"
                    ).build()
                }
            }
            return instance as StoryRoomDatabase
        }
    }
}