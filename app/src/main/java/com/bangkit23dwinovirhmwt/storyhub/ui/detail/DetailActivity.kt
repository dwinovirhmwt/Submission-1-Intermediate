package com.bangkit23dwinovirhmwt.storyhub.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit23dwinovirhmwt.storyhub.data.model.Result
import com.bangkit23dwinovirhmwt.storyhub.data.remote.response.Story
import com.bangkit23dwinovirhmwt.storyhub.databinding.ActivityDetailBinding
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.login.LoginActivity
import com.bangkit23dwinovirhmwt.storyhub.ui.factory.MainModelFactory
import com.bangkit23dwinovirhmwt.storyhub.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private val storyViewModel by viewModels<DetailViewModel> {
        MainModelFactory.getInstance(this@DetailActivity)
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetailActivity)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra("id")

        lifecycleScope.launch {
            storyViewModel.getSession().collect { user ->
                if (!user.isLogin) {
                    startActivity(Intent(this@DetailActivity, LoginActivity::class.java))
                    finish()
                } else {
                    storyViewModel.getDetailStory(user.token, id.toString())
                        .observe(this@DetailActivity) { story ->
                            setStoryContent(story)
                        }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setStoryContent(story: Result<Story?>?) {
        if (story != null) {
            when (story) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    Glide.with(this@DetailActivity).load(story.data?.photoUrl)
                        .transform(CenterCrop(), RoundedCorners(30))
                        .into(binding.ivDetail)

                    binding.apply {
                        tvTitleDescription.text = story.data?.name
                        tvDescription.text = story.data?.description
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this, story.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}