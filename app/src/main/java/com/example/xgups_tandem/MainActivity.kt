package com.example.xgups_tandem

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.xgups_tandem.databinding.ActivityMainBinding
import com.example.xgups_tandem.api.*
import com.example.xgups_tandem.api.SamGUPS.SamGUPS
import kotlinx.coroutines.launch
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.fragmentContainerView)
        return super.onSupportNavigateUp()
    }
}