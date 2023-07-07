package com.example.weatheapp.splash

import MyLocation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weatheapp.intialsetup.IntialSetupFragment
import com.example.weatheapp.R
import com.example.weatheapp.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val myLocation = MyLocation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

       /* val mySharedPreferences = MySharedPreferences.getInstance(this)
        val language = mySharedPreferences.getLanguagePreference()
        if (language == null) {
            mySharedPreferences.saveLanguagePreference(Constants.en.toString())
        }
        Log.i("TAG", "onCreate: $language")*/

        myLocation.loadFromSharedPreferences(this@SplashActivity)
        Log.d("splash", "Latitude: ${myLocation.lat}, Longitude: ${myLocation.lng}")
        GlobalScope.launch(Dispatchers.Main) {
            delay(2700)
            if (myLocation.lat == 0.0 && myLocation.lng == 0.0) {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.intialSetupFragment_container, IntialSetupFragment())
                fragmentTransaction.commit()
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }
    }

}