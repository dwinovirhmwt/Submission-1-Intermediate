package com.bangkit23dwinovirhmwt.storyhub.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit23dwinovirhmwt.storyhub.data.model.StoryHubRepository
import com.bangkit23dwinovirhmwt.storyhub.di.Injection
import com.bangkit23dwinovirhmwt.storyhub.ui.detail.DetailViewModel
import com.bangkit23dwinovirhmwt.storyhub.ui.main.MainViewModel
import com.bangkit23dwinovirhmwt.storyhub.ui.upload.UploadViewModel

class MainModelFactory private constructor(private val storyHubRepository: StoryHubRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyHubRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyHubRepository) as T
        }
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(storyHubRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MainModelFactory? = null

        fun getInstance(context: Context): MainModelFactory =
            instance ?: synchronized(this) {
                instance ?: MainModelFactory(Injection.storyProvideRepository(context))
            }.also { instance = it }
    }
}