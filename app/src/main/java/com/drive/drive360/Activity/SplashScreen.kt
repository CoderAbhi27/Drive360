package com.drive.drive360.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import com.drive.drive360.R
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class splashScreen : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private val splash_Timeout: Int = 2000
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        titleTextView=findViewById(R.id.SplashLogo)
        animateText(titleTextView.text.toString())
        Handler(Looper.getMainLooper()).postDelayed({

            val i = Intent(
                this@splashScreen,
                LoginActivity::class.java
            )
            startActivity(i)
            finish()
        }, splash_Timeout.toLong())
    }

    private fun animateText(text: String) {

        if (i <= text.length) {
            val fetchtext: String = text.substring(0, i);
            titleTextView.text = fetchtext
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    i++;
                    animateText(text)
                }, 100
            )
        }
    }
}