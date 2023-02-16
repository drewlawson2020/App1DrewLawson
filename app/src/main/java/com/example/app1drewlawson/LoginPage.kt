package com.example.app1drewlawson

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginPage : AppCompatActivity() {
    var mTvFullName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var textToDisplay: String? = null;
        //Get the text views
        mTvFullName = findViewById<View>(R.id.tv_fulln_data) as TextView

        //Get the starter intent
        val receivedIntent = intent
        textToDisplay = receivedIntent.getStringExtra("ET_STRING") + " is logged in!";
        //Set the text views
        mTvFullName!!.text = textToDisplay;

    }
}