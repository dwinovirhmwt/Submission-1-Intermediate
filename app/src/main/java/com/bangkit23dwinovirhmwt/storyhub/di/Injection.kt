package com.bangkit23dwinovirhmwt.storyhub.di

import android.content.Context
import com.bangkit23dwinovirhmwt.storyhub.data.local.room.StoryRoomDatabase
import com.bangkit23dwinovirhmwt.storyhub.data.model.AuthorizeRepository
import com.bangkit23dwinovirhmwt.storyhub.data.model.StoryHubRepository
import com.bangkit23dwinovirhmwt.storyhub.data.preference.UserPreferences
import com.bangkit23dwinovirhmwt.storyhub.data.preference.dataStore
import com.bangkit23dwinovirhmwt.storyhub.data.remote.retrofit.ApiConfig
import com.bangkit23dwinovirhmwt.storyhub.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): AuthorizeRepository {
        val apiService = ApiConfig.getApiService()
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        return AuthorizeRepository.getInstance(apiService, userPreferences)
    }

    fun storyProvideRepository(context: Context): StoryHubRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryRoomDatabase.getInstance(context)
        val dao = database.storyHubDao()
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        return StoryHubRepository.getInstance(apiService, dao, userPreferences, appExecutors)
    }
}