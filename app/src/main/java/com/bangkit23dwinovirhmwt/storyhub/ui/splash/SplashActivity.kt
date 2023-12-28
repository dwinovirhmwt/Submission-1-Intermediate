package com.bangkit23dwinovirhmwt.storyhub.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit23dwinovirhmwt.storyhub.R
import com.bangkit23dwinovirhmwt.storyhub.ui.authorize.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val splashScreen = Thread {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        splashScreen.start()
    }

}