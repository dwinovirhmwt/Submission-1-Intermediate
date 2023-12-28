package com.bangkit23dwinovirhmwt.storyhub.ui.authorize.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.core.widget.doOnTextChanged
import com.bangkit23dwinovirhmwt.storyhub.databinding.ActivityRegisterBinding
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.AuthorizeViewModel
import com.bangkit23dwinovirhmwt.storyhub.ui.factory.AuthorizeModelFactory
import kotlinx.coroutines.launch
import com.bangkit23dwinovirhmwt.storyhub.data.model.Result
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel by viewModels<AuthorizeViewModel> {
        AuthorizeModelFactory.getInstance(this)
    }

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initialForm()
        setFormValues()
        playAnimation()
        setupAction()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding?.ivLogoRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            startDelay = 100
        }.start()

        val titleTextView =
            ObjectAnimator.ofFloat(binding?.titleTextView, View.ALPHA, 1f).setDuration(300)
        val nameEditText =
            ObjectAnimator.ofFloat(binding?.nameEditText, View.ALPHA, 1f).setDuration(300)
        val emailEditText =
            ObjectAnimator.ofFloat(binding?.emailEditText, View.ALPHA, 1f).setDuration(300)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding?.passwordEditText, View.ALPHA, 1f).setDuration(300)
        val signupButton =
            ObjectAnimator.ofFloat(binding?.registerButton, View.ALPHA, 1f).setDuration(300)
        val tvHaveAccount =
            ObjectAnimator.ofFloat(binding?.tvHaveAccount, View.ALPHA, 1f).setDuration(300)
        val tvLogin =
            ObjectAnimator.ofFloat(binding?.tvLogin, View.ALPHA, 1f).setDuration(300)


        val together1 = AnimatorSet().apply {
            playTogether(
                titleTextView,
                nameEditText,
                emailEditText,
                passwordEditText
            )
        }

        val together2 = AnimatorSet().apply {
            playTogether(signupButton, tvHaveAccount, tvLogin)
        }

        AnimatorSet().apply {
            playSequentially(
                together1, together2
            )
            startDelay = 600
            start()
        }
    }

    private fun setupAction() {
        binding?.registerButton?.setOnClickListener {
            getFormValues()
            if (registerViewModel.validateRegister()) {
                processRegistration()
            }
        }

        binding?.tvLogin?.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getFormValues() {
        name = registerViewModel.name.toString()
        email = registerViewModel.email.toString()
        password = registerViewModel.password.toString()
    }

    private fun setFormValues() {
        binding?.nameEditText?.editText?.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setNameValue(
                text.toString()
            )
        }

        binding?.emailEditText?.editText?.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setEmailValue(text.toString())
        }

        binding?.passwordEditText?.editText?.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setPasswordValue(text.toString())
        }
    }

    private fun initialForm() {
        binding?.apply {
            nameEditText.editText?.setText(registerViewModel.name)
            emailEditText.editText?.setText(registerViewModel.email)
            passwordEditText.editText?.setText(registerViewModel.password)
        }
    }

    private fun processRegistration() {
        lifecycleScope.launch {
            registerViewModel.registerUser(name, email, password)
                .observe(this@RegisterActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(
                                    this@RegisterActivity, result.data?.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                registerViewModel.clearRegister()
                                initialForm()

                                val intent =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }

                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(
                                    this@RegisterActivity, result.error,
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