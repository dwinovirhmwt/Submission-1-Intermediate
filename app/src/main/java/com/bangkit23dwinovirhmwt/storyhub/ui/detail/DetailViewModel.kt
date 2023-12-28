package com.bangkit23dwinovirhmwt.storyhub.ui.detail

import androidx.lifecycle.ViewModel
import com.bangkit23dwinovirhmwt.storyhub.data.model.StoryHubRepository

class DetailViewModel(private val storyHubRepository: StoryHubRepository) : ViewModel() {

    fun getDetailStory(token: String, id: String) = storyHubRepository.getDetailStories(token, id)

    fun getSession() = storyHubRepository.getSession()
}