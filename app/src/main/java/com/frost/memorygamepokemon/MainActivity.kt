package com.frost.memorygamepokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.memorygamepokemon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customDialog : CustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customDialog = CustomDialog(this, layoutInflater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playButton.setOnClickListener { setDialog() }
    }

    private fun setDialog() {
        customDialog.showDifficulty()
        customDialog.onDifficultyButtonCallback = { startGame(it) }
    }

    private fun startGame(difficulty: Int) {
        customDialog.dismiss()
        BoardActivity.start(this, difficulty)
    }
}