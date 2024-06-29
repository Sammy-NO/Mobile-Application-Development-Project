package com.cns.postnatalcare


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCreateProfile: Button = findViewById(R.id.buttonCreateProfile)
        buttonCreateProfile.setOnClickListener {
            val intent = Intent(this, CreateChildProfileActivity::class.java)
            startActivity(intent)
        }
    }
}

