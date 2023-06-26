package com.example.weatheapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.weatheapp.mainactivity.MainActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000/*
    lateinit var  dialog :Dialog
    lateinit var okBtn:MaterialButton*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

      /*   dialog = Dialog(this)

        dialog.setContentView(R.layout.intial_setup_dialog)
        okBtn = dialog.findViewById(R.id.splashOkBtn)

        Handler().postDelayed({
            dialog.show()
            finish()
        }, SPLASH_TIME_OUT)

        okBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }*/

        GlobalScope.launch(Dispatchers.Main) {
            delay(2700)
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.intialSetupFragment_container, IntialSetupFragment())
            fragmentTransaction.commit()
        }
            }
    }