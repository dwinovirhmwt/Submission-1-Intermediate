package com.bangkit23dwinovirhmwt.storyhub.ui.upload

import androidx.lifecycle.ViewModel
import com.bangkit23dwinovirhmwt.storyhub.data.model.StoryHubRepository
import java.io.File

class UploadViewModel(private val storyHubRepository: StoryHubRepository) : ViewModel() {
    var description: String? = null

    fun setDescriptionValue(description: String) {
        this.description = description
    }

    fun validate(): Boolean {
        return description != null
    }

    fun getSession() = storyHubRepository.getSession()

    fun addStory(token: String, file: File, description: String) =
        storyHubRepository.addStory(token, file, description)
}