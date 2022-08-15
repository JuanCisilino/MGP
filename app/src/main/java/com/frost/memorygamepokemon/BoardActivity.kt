package com.frost.memorygamepokemon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.memorygamepokemon.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding
    private lateinit var difficulty: String

    companion object{
        private const val difficultyKey = "difficulty"
        fun start(context: Context, difficulty: String){
            val intent = Intent(context, BoardActivity::class.java).apply {
                putExtra(difficultyKey, difficulty)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        difficulty = intent.getStringExtra(difficultyKey)!!
        initMembers()
    }

    private fun initMembers() {
        binding.backButton.setOnClickListener { onBackPressed() }
    }
}