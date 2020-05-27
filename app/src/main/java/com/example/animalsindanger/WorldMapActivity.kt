package com.example.animalsindanger

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.animalsindanger.MainActivity.Companion.lastActivity

class WorldMapActivity : AppCompatActivity() {

    private lateinit var buttonReturn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.world_map)
        buttonReturn = findViewById(R.id.imageBack)
        buttonReturn.setOnClickListener {
            setLastView()
        }
    }

    private fun setLastView() {
        val act: Activity = lastActivity.removeAt(lastActivity.lastIndex)
        val intent = Intent(this, act::class.java)
        startActivity(intent)
    }
}
