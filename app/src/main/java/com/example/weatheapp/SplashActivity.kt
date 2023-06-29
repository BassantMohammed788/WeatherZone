package com.example.weatheapp

import MyLocation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weatheapp.mainactivity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val myLocation = MyLocation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        myLocation.loadFromSharedPreferences(this@SplashActivity)
        Log.d("splash", "Latitude: ${myLocation.lat}, Longitude: ${myLocation.lng}")
        GlobalScope.launch(Dispatchers.Main) {
            delay(2700)
            if(myLocation.lat == 0.0 && myLocation.lng == 0.0) {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.intialSetupFragment_container, IntialSetupFragment())
                fragmentTransaction.commit()
            }else{
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
            }
    }