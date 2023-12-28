package com.bangkit23dwinovirhmwt.storyhub.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit23dwinovirhmwt.storyhub.data.model.StoryHubRepository
import kotlinx.coroutines.launch

class MainViewModel(private val storyHubRepository: StoryHubRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            storyHubRepository.logout()
        }
    }

    fun getSession() = storyHubRepository.getSession()

    fun getAllStories(token: String) = storyHubRepository.getAllStories(token)
}