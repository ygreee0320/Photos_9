package com.example.prography

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.prography.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toolbar
        setSupportActionBar(binding.toolbar)

        //bottom navigator
        binding.bottomMenu.setOnItemSelectedListener {item ->
            changeFragment(
                when (item.itemId) {
                    R.id.home -> HomeFragment()
                    else -> RandomFragment()
                }
            )
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .commit()
    }
}