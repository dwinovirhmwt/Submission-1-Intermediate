package com.bangkit23dwinovirhmwt.storyhub.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit23dwinovirhmwt.storyhub.R
import com.bangkit23dwinovirhmwt.storyhub.adapter.StoryHubAdapter
import com.bangkit23dwinovirhmwt.storyhub.data.local.entity.StoryEntity
import com.bangkit23dwinovirhmwt.storyhub.databinding.ActivityMainBinding
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.login.LoginActivity
import com.bangkit23dwinovirhmwt.storyhub.ui.factory.MainModelFactory
import com.bangkit23dwinovirhmwt.storyhub.data.model.Result
import com.bangkit23dwinovirhmwt.storyhub.ui.upload.UploadActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val storyViewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(this)
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private lateinit var storyHubAdapter: StoryHubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        storyHubAdapter = StoryHubAdapter()

        lifecycleScope.launch {
            storyViewModel.getSession().collect { user ->
                if (!user.isLogin) {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                } else {
                    storyViewModel.getAllStories(user.token).observe(this@MainActivity) { result ->
                        showStories(result)
                    }
                }
            }
        }

        binding?.rvStory?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        binding?.btnAdd?.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadActivity::class.java))
        }
    }

    private fun showStories(result: Result<List<StoryEntity>>?) {
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    val storiesData = result.data
                    storyHubAdapter.submitList(storiesData)
                    binding?.rvStory?.adapter = storyHubAdapter
                }

                is Result.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(
                        this, result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_logout -> {
                val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
                val scope = CoroutineScope(dispatcher)
                scope.launch {
                    storyViewModel.logout()
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}