package com.example.cinergyapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.cinergyapp.R
import com.example.cinergyapp.utils.AppPreference
import com.example.cinergyapp.utils.USER_ID

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        Handler(Looper.getMainLooper()).postDelayed({
            if(!AppPreference.getString(USER_ID).isNullOrEmpty()){
                startActivity(Intent(this@SplashActivity , EscapeRoomActivity::class.java))
                this.finish()
            }else{
                startActivity(Intent(this@SplashActivity , MainActivity::class.java))
                this.finish()
            }
        }, 3000)
    }
}