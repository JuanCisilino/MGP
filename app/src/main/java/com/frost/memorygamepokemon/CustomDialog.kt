package com.frost.memorygamepokemon

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso

class CustomDialog(context: Context): DialogFragment() {

    private var dialogView: View
    private lateinit var endGameLayout: LinearLayoutCompat
    private lateinit var endGameImage: ShapeableImageView
    private lateinit var endGameMessage: MaterialTextView
    private lateinit var endGameButton: MaterialButton
    private lateinit var difficultyLayout: LinearLayoutCompat
    private lateinit var easyButton: MaterialButton
    private lateinit var mediumButton: MaterialButton
    private lateinit var hardButton: MaterialButton
    private lateinit var alertDialog: AlertDialog
    var onEndGameButtonCallback : (() -> Unit)? = null
    var onDifficultyButtonCallback : ((difficulty: String) -> Unit)? = null

    init {
        val builder = AlertDialog.Builder(context)
        dialogView = layoutInflater.inflate(R.layout.dialog, null)
        builder.setView(dialogView)
        alertDialog = builder.create()
        initMembers()

    }

    fun showDifficulty(){
        endGameLayout.visibility = View.GONE
        easyButton.setOnClickListener { onDifficultyButtonCallback?.invoke("easy") }
        mediumButton.setOnClickListener { onDifficultyButtonCallback?.invoke("medium") }
        hardButton.setOnClickListener { onDifficultyButtonCallback?.invoke("hard") }
        show()
    }

    fun showEndGame(gameLost: Boolean){
        difficultyLayout.visibility = View.GONE
        if(gameLost) {
            Picasso.get()
                .load(R.drawable.loser)
                .into(endGameImage)
            endGameMessage.text = getString(R.string.looser_message)
            endGameButton.text = getString(R.string.retry)
        } else {
            Picasso.get()
                .load(R.drawable.winner)
                .into(endGameImage)
            endGameMessage.text = getString(R.string.winner_message)
            endGameButton.text = getString(R.string.finish)
        }
        endGameButton.setOnClickListener {
            alertDialog.dismiss()
            onEndGameButtonCallback?.invoke()
        }
        alertDialog.setCancelable(false)
        show()
    }

    private fun show(){
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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