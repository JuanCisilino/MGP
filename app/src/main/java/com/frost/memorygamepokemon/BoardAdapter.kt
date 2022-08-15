package com.frost.memorygamepokemon

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class BoardAdapter: RecyclerView.Adapter<BoardAdapter.CardHolder>() {

    private var pokelist = ArrayList<Pokemon>()
    private lateinit var context: Context
    var onCardClickCallback: ((pokemon: Pokemon, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardHolder(inflater)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(pokelist[position])
    }

    override fun getItemCount():Int = pokelist.size

    inner class CardHolder(private val view: View): RecyclerView.ViewHolder(view){

        private var isBlockSide = true
        private val blockItem = view.findViewById<MaterialCardView>(R.id.card_item)
        private val frontBlockImage = view.findViewById<ShapeableImageView>(R.id.front_card_image)
        private val backBlockImage = view.findViewById<ShapeableImageView>(R.id.back_card_image)

        fun bind(pokemon: Pokemon) {
            Picasso.get()
                .load(pokemon.backImage)
                .into(backBlockImage)
            if(pokemon.backImage == R.drawable.pokeball) {
                frontBlockImage.visibility = View.GONE
                backBlockImage.visibility = View.VISIBLE
                backBlockImage.isEnabled = false
            } else {
                if(!isBlockSide)
                    frontBlockImage.scaleX = -1f
                isBlockSide = true
                frontBlockImage.visibility = View.VISIBLE
                backBlockImage.visibility = View.GONE
            }
            setUpAnimation(context, pokemon, position)
        }

        private fun setUpAnimation(context: Context, item: Pokemon, position: Int) {
            val animator by lazy { AnimatorInflater.loadAnimator(context, R.animator.flip) }
            blockItem.setOnClickListener {
                onCardClickCallback?.invoke(item, position)
                animator.setTarget(if (isBlockSide) frontBlockImage else backBlockImage)
                animator.start()
                animator.addListener(object: Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {
                        if (isBlockSide)
                            frontBlockImage.isEnabled = false
                        else
                            backBlockImage.isEnabled = false
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        if (isBlockSide) {
                            isBlockSide = false
                            frontBlockImage.isEnabled = true
                            frontBlockImage.visibility = View.GONE
                            backBlockImage.visibility = View.VISIBLE
                        } else {
                            isBlockSide = true
                            backBlockImage.isEnabled = true
                            backBlockImage.visibility = View.GONE
                            frontBlockImage.visibility = View.VISIBLE
                        }
                    }

                    override fun onAnimationCancel(p0: Animator?) { }

                    override fun onAnimationRepeat(p0: Animator?) { }

                })
            }
        }
    }

    fun setList(list: List<Pokemon>, context: Context){
        pokelist = list as java.util.ArrayList<Pokemon>
        this.context = context
        this.notifyDataSetChanged()
    }
}