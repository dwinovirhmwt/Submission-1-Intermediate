package com.bangkit23dwinovirhmwt.storyhub.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit23dwinovirhmwt.storyhub.data.model.AuthorizeRepository
import com.bangkit23dwinovirhmwt.storyhub.di.Injection
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.AuthorizeViewModel

class AuthorizeModelFactory private constructor(private val authRepository: AuthorizeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorizeViewModel::class.java)) {
            return AuthorizeViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthorizeModelFactory? = null

        fun getInstance(context: Context): AuthorizeModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthorizeModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}