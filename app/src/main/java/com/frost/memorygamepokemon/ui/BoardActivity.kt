package com.frost.memorygamepokemon.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.memorygamepokemon.App.Companion.EASY_COLUMNS
import com.frost.memorygamepokemon.App.Companion.EASY_DIFFICULT
import com.frost.memorygamepokemon.App.Companion.EASY_TOTAL_BLOCKS
import com.frost.memorygamepokemon.App.Companion.HARD_COLUMNS
import com.frost.memorygamepokemon.App.Companion.HARD_DIFFICULT
import com.frost.memorygamepokemon.App.Companion.HARD_TOTAL_BLOCKS
import com.frost.memorygamepokemon.App.Companion.MEDIUM_COLUMNS
import com.frost.memorygamepokemon.App.Companion.MEDIUM_DIFFICULT
import com.frost.memorygamepokemon.App.Companion.MEDIUM_TOTAL_BLOCKS
import com.frost.memorygamepokemon.helpers.BoardAdapter
import com.frost.memorygamepokemon.helpers.CustomDialog
import com.frost.memorygamepokemon.model.Pokemon
import com.frost.memorygamepokemon.R
import com.frost.memorygamepokemon.databinding.ActivityBoardBinding
import com.squareup.picasso.Picasso
import ir.samanjafari.easycountdowntimer.CountDownInterface
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding
    private var difficulty by Delegates.notNull<Int>()
    private val adapter by lazy { BoardAdapter() }
    private var columns = EASY_COLUMNS
    private var totalBlocks = EASY_TOTAL_BLOCKS
    private var remainingPairs = EASY_TOTAL_BLOCKS/2
    private var movements = 0
    private var timerStarted = false
    private var cards = ArrayList<Pokemon>()
    private var rndIdsUsed = ArrayList<Int>()
    private var cardSelected: Pokemon? = null
    private var isValidating = false
    private lateinit var customDialog : CustomDialog

    companion object{
        private const val difficultyKey = "difficulty"
        fun start(context: Context, difficulty: Int){
            val intent = Intent(context, BoardActivity::class.java).apply {
                putExtra(difficultyKey, difficulty)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customDialog = CustomDialog(this, layoutInflater)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        difficulty = intent.getIntExtra(difficultyKey,0)
        initMembers()
    }

    private fun initMembers() {
        customDialog.onEndGameButtonCallback = { onBackPressed() }
        binding.backButton.setOnClickListener { onBackPressed() }
        when(difficulty) {
            EASY_DIFFICULT -> {
                columns = EASY_COLUMNS
                totalBlocks = EASY_TOTAL_BLOCKS
            }
            MEDIUM_DIFFICULT -> {
                columns = MEDIUM_COLUMNS
                totalBlocks = MEDIUM_TOTAL_BLOCKS
            }
            HARD_DIFFICULT -> {
                columns = HARD_COLUMNS
                totalBlocks = HARD_TOTAL_BLOCKS
            }
        }
        remainingPairs = totalBlocks/2
        setAdapter()
    }

    private fun setAdapter(){
        adapter.onCardClickCallback = { pokemon, position ->  validateCard(pokemon, position) }
        adapter.setList(buildList(), this)
        initUI()
    }

    private fun buildList(): ArrayList<Pokemon>{
        val characters = resources.obtainTypedArray(R.array.characters)
        while ( cards.size < totalBlocks) {
            val rndIndex = Random().nextInt(characters.length())
            val resID = characters.getResourceId(rndIndex, 0)
            if(rndIndex !in rndIdsUsed) {
                rndIdsUsed.add(rndIndex)
                cards.add(Pokemon(cards.size, resID))
                cards.add(Pokemon(cards.size, resID))
            }
        }
        return ArrayList(cards.shuffled())
    }

    private fun initUI() {
        binding.countDown.setOnTick(object: CountDownInterface {
            override fun onTick(time: Long) { timerStarted = true }

            override fun onFinish() {
                if(remainingPairs > 0)
                    customDialog.showEndGame(gameLost = true, this@BoardActivity)
                else
                    customDialog.showEndGame(gameLost = false, this@BoardActivity)
            }

        })
        binding.cardsRecycler.layoutManager = GridLayoutManager(
            this,
            columns,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.cardsRecycler.setHasFixedSize(true)
        binding.cardsRecycler.adapter = adapter
        updateValues()
    }

    private fun updateValues() {
        Picasso.get()
            .load(R.drawable.default_background)
            .into(binding.firstSelection)
        Picasso.get()
            .load(R.drawable.default_background)
            .into(binding.secondSelection)
        binding.movements.text = movements.toString()
        binding.remainingPairs.text = remainingPairs.toString()
        cardSelected = null
        adapter.notifyDataSetChanged()
        isValidating = false
        if (remainingPairs == 0) {
            binding.countDown.pause()
            customDialog.showEndGame(gameLost = false, this)
        }
    }

    private fun validateCard(pokemon: Pokemon, position: Int) {
        if(!isValidating) {
            if(!timerStarted)
                binding.countDown.startTimer()
            Handler(Looper.getMainLooper()).postDelayed({
                if(cardSelected == null) {
                    cardSelected = pokemon
                    showCardSelected(pokemon.backImage, isFirstBlock = true)
                } else if(cardSelected != pokemon) {
                    isValidating = true
                    showCardSelected(pokemon.backImage, isFirstBlock = false)
                    Handler(Looper.getMainLooper()).postDelayed({validateCardsSelected(pokemon)}, 600)
                }
            }, 200)
        }
    }

    private fun validateCardsSelected(itemSelected: Pokemon) {
        movements++
        if(cardSelected!!.backImage == itemSelected.backImage) {
            remainingPairs--
            cards[cards.indexOf(itemSelected)].backImage = R.drawable.default_background
            cards[cards.indexOf(cardSelected)].backImage = R.drawable.default_background
        }
        updateValues()
    }

    private fun showCardSelected(resourceId: Int, isFirstBlock: Boolean) {
        Picasso.get()
            .load(resourceId)
            .into(if(isFirstBlock) binding.firstSelection else binding.secondSelection)
    }
}