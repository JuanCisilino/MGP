package com.frost.memorygamepokemon.model

import androidx.annotation.DrawableRes

data class Pokemon(
    val imageId: Int,
    @DrawableRes var backImage: Int
)
