package com.example.animalsindanger

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    companion object {
        var lastActivity: MutableList<Activity> = arrayListOf()
    }

    private lateinit var buttonGeolocate: Button
    private lateinit var buttonWorldmap: Button
    private lateinit var buttonSearchanimals: Button

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We set val because we don't want to change them.
        buttonGeolocate = findViewById<Button>(R.id.button0)
        buttonGeolocate.setOnClickListener {
            lastActivity.add(this)
            val intent = Intent(this, GeolocateActivity::class.java)
            startActivity(intent)
        }

        buttonWorldmap = findViewById<Button>(R.id.button1)
        buttonWorldmap.setOnClickListener {
            lastActivity.add(this)
            val intent = Intent(this, WorldMapActivity::class.java)
            startActivity(intent)
        }

        buttonSearchanimals = findViewById<Button>(R.id.button2)
        buttonSearchanimals.setOnClickListener {
            lastActivity.add(this)
            val intent = Intent(this, SearchAnimalsActivity::class.java)
            startActivity(intent)
        }

    }

}

