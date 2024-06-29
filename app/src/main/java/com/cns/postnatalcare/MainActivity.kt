package com.cns.postnatalcare


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCreateChildProfile: Button = findViewById(/* id = */ R.id.buttonCreateChildProfile)
        buttonCreateChildProfile.setOnClickListener {
            val intent = Intent(this, CreateChildProfileActivity::class.java)
            startActivity(intent)
        }

        val buttonCreateVaccineProfile: Button = findViewById(R.id.buttonCreateVaccineProfile)
        buttonCreateVaccineProfile.setOnClickListener {
            val intent = Intent(this, CreateVaccineProfileActivity::class.java)
            startActivity(intent)
        }

    }
}

