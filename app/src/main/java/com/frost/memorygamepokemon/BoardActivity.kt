package com.frost.memorygamepokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.memorygamepokemon.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}