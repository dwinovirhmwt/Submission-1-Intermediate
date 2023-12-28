package com.bangkit23dwinovirhmwt.storyhub.ui.authorize.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bangkit23dwinovirhmwt.storyhub.databinding.ActivityLoginBinding
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.AuthorizeViewModel
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.register.RegisterActivity
import com.bangkit23dwinovirhmwt.storyhub.ui.factory.AuthorizeModelFactory
import com.bangkit23dwinovirhmwt.storyhub.ui.main.MainActivity
import kotlinx.coroutines.launch
import com.bangkit23dwinovirhmwt.storyhub.data.model.Result

class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<AuthorizeViewModel> {
        AuthorizeModelFactory.getInstance(this)
    }

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        lifecycleScope.launch {
            loginViewModel.getSession().collect { user ->
                if (user.isLogin) {
                    loginViewModel.clearLogin()
                    initialForm()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }

        playAnimation()
        initialForm()
        setupAction()
        setFormValues()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding?.ivLogoLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            startDelay = 300
        }.start()

        val titleTextView =
            ObjectAnimator.ofFloat(binding?.titleTextView, View.ALPHA, 1f).setDuration(300)
        val messageTextView =
            ObjectAnimator.ofFloat(binding?.messageTextView, View.ALPHA, 1f).setDuration(300)
        val emailEditText =
            ObjectAnimator.ofFloat(binding?.emailEditText, View.ALPHA, 1f).setDuration(300)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding?.passwordEditText, View.ALPHA, 1f).setDuration(300)
        val loginButton =
            ObjectAnimator.ofFloat(binding?.loginButton, View.ALPHA, 1f).setDuration(300)
        val tvNoHaveAccount =
            ObjectAnimator.ofFloat(binding?.tvNoHaveAccount, View.ALPHA, 1f).setDuration(300)
        val tvRegister =
            ObjectAnimator.ofFloat(binding?.tvRegister, View.ALPHA, 1f).setDuration(300)

        val together1 = AnimatorSet().apply {
            playTogether(
                titleTextView,
                messageTextView,
                emailEditText,
                passwordEditText
            )
        }

        val together2 = AnimatorSet().apply {
            playTogether(loginButton, tvNoHaveAccount, tvRegister)
        }

        AnimatorSet().apply {
            playSequentially(
                together1,
                together2
            )
            startDelay = 600
            start()
        }
    }

    private fun setupAction() {
        binding?.loginButton?.setOnClickListener {
            getFormValues()
            if (loginViewModel.validateLogin()) {
                processLogin()
            }
        }

        binding?.tvRegister?.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getFormValues() {
        email = loginViewModel.email.toString()
        password = loginViewModel.password.toString()
    }

    private fun setFormValues() {
        binding?.emailEditText?.editText?.doOnTextChanged { text, _, _, _ ->
            loginViewModel.setEmailValue(
                text.toString())
        }

        binding?.passwordEditText?.editText?.doOnTextChanged { text, _, _, _ ->
            loginViewModel.setPasswordValue(
                text.toString())
        }
    }

    private fun initialForm() {
        binding?.apply {
            emailEditText.editText?.setText(loginViewModel.email)
            passwordEditText.editText?.setText(loginViewModel.password)
        }
    }

    private fun processLogin() {
        lifecycleScope.launch {
            loginViewModel.loginUser(email, password).observe(this@LoginActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity, result.data?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity, result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}