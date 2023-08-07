package com.example.sagarmiles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import com.example.drive360.Activity.LoginActivity

class splashScreen : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private val splash_Timeout: Int = 2000
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

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