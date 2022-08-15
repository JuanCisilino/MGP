package com.frost.memorygamepokemon

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso

class CustomDialog(context: Context, layoutInflater: LayoutInflater) {

    private lateinit var dialogView: View
    private lateinit var endGameLayout: LinearLayout
    private lateinit var endGameImage: ShapeableImageView
    private lateinit var endGameMessage: MaterialTextView
    private lateinit var endGameButton: MaterialButton
    private lateinit var difficultyLayout: LinearLayout
    private lateinit var easyButton: MaterialButton
    private lateinit var mediumButton: MaterialButton
    private lateinit var hardButton: MaterialButton
    private lateinit var alertDialog: AlertDialog
    var onEndGameButtonCallback : (() -> Unit)? = null
    var onDifficultyButtonCallback : ((difficulty: Int) -> Unit)? = null

    init {
        create(context, layoutInflater)
    }

    private fun create(context: Context, layoutInflater: LayoutInflater) {
        dialogView = layoutInflater.inflate(R.layout.dialog, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        initMembers()
        alertDialog = builder.create()
    }

    fun showDifficulty(){
        endGameLayout.visibility = View.GONE
        difficultyLayout.visibility = View.VISIBLE
        easyButton.setOnClickListener { onDifficultyButtonCallback?.invoke(App.EASY_DIFFICULT) }
        mediumButton.setOnClickListener { onDifficultyButtonCallback?.invoke(App.MEDIUM_DIFFICULT) }
        hardButton.setOnClickListener { onDifficultyButtonCallback?.invoke(App.HARD_DIFFICULT) }
        showDialog()
    }

    fun showEndGame(gameLost: Boolean, context: Context){
        difficultyLayout.visibility = View.GONE
        endGameLayout.visibility = View.VISIBLE
        if(gameLost) {
            Picasso.get()
                .load(R.drawable.loser)
                .into(endGameImage)
            endGameMessage.text = context.getText(R.string.looser_message)
            endGameButton.text = context.getText(R.string.retry)
        } else {
            Picasso.get()
                .load(R.drawable.winner)
                .into(endGameImage)
            endGameMessage.text = context.getText(R.string.winner_message)
            endGameButton.text = context.getText(R.string.finish)
        }
        endGameButton.setOnClickListener {
            alertDialog.dismiss()
            onEndGameButtonCallback?.invoke()
        }
        alertDialog.setCancelable(false)
        showDialog()
    }

    private fun showDialog(){
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun dismiss(){
        alertDialog.dismiss()
    }

    private fun initMembers(){
        endGameLayout = dialogView.findViewById(R.id.end_game_layout)
        endGameImage = dialogView.findViewById(R.id.end_game_image)
        endGameMessage = dialogView.findViewById(R.id.end_game_message)
        endGameButton = dialogView.findViewById(R.id.end_game_button)
        difficultyLayout = dialogView.findViewById(R.id.difficulty_layout)
        easyButton = dialogView.findViewById(R.id.easy_button)
        mediumButton = dialogView.findViewById(R.id.medium_button)
        hardButton = dialogView.findViewById(R.id.hard_button)
    }
}