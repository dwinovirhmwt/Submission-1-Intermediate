package com.bangkit23dwinovirhmwt.storyhub.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bangkit23dwinovirhmwt.storyhub.R
import com.bangkit23dwinovirhmwt.storyhub.databinding.ActivityUploadBinding
import com.bangkit23dwinovirhmwt.storyhub.ui.factory.MainModelFactory
import kotlinx.coroutines.launch
import com.bangkit23dwinovirhmwt.storyhub.data.model.Result
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.login.LoginActivity
import com.bangkit23dwinovirhmwt.storyhub.ui.camera.CameraActivity
import com.bangkit23dwinovirhmwt.storyhub.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.bangkit23dwinovirhmwt.storyhub.ui.camera.reduceFileImage
import com.bangkit23dwinovirhmwt.storyhub.ui.camera.uriToFile
import com.bangkit23dwinovirhmwt.storyhub.ui.main.MainActivity

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null

    private lateinit var description: String

    private val viewModel by viewModels<UploadViewModel> {
        MainModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_granted),
                    Toast.LENGTH_LONG,
                ).show()
                Log.d("Check", "Permission dikabulkan")
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_denied),
                    Toast.LENGTH_LONG
                ).show()
                Log.d("Check", "Permission ditolak")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        initialForm()
        setFormValue()

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUploadImage.setOnClickListener { fileUpload() }
    }

    private fun setFormValue() {
        binding.edAddDescription.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.setDescriptionValue(text.toString())
        }
    }

    private fun initialForm() {
        binding.apply {
            edAddDescription.editText?.setText(viewModel.description)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun fileUpload() {
        description = viewModel.description.toString()
        if (viewModel.validate()) {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                lifecycleScope.launch {
                    viewModel.getSession().collect {
                        if (!it.isLogin) {
                            startActivity(Intent(this@UploadActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            viewModel.addStory(it.token, imageFile, description)
                                .observe(this@UploadActivity) { result ->
                                    if (result != null) {
                                        when (result) {
                                            is Result.Loading -> showLoading(true)
                                            is Result.Success -> {
                                                showToast(result.data?.message.toString())
                                                showLoading(false)
                                                val intent = Intent(
                                                    this@UploadActivity,
                                                    MainActivity::class.java
                                                )
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                startActivity(intent)
                                                finish()
                                            }

                                            is Result.Error -> {
                                                showToast(result.error)
                                                showLoading(false)
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            } ?: showToast(getString(R.string.failed_upload_image))
        } else {
            showToast(getString(R.string.error_field_required))
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, getString(R.string.no_media_selected), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imgUploadStory.setImageURI(it)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}