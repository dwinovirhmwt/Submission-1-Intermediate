package com.bangkit23dwinovirhmwt.storyhub.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit23dwinovirhmwt.storyhub.data.local.entity.StoryEntity

@Dao
interface StoryHubDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: List<StoryEntity>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): LiveData<List<StoryEntity>>

    @Query("DELETE FROM stories")
    fun deleteAll()
}