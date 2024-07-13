package com.cns.postnatalapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity<BottomNavigationView : View?> : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load the default fragment
        loadFragment(OutpatientSchedulingFragment())

        findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setOnNavigationItemSelectedListener()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

private fun setOnNavigationItemSelectedListener() {

}

