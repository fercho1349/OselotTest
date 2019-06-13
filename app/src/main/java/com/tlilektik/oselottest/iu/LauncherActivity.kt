package com.tlilektik.oselottest.iu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.tlilektik.oselottest.R

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed(
            {
                // After the splash screen duration, route to the right activities
                val sp = getSharedPreferences("LOGIN",Context.MODE_PRIVATE)
                if(sp.getString("LogIn","0") == "Succes"){
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                finish()
            },
            splashScreenDuration
        )
    }

    private fun getSplashScreenDuration() = 5000L

}