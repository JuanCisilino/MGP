package com.frost.memorygamepokemon

import androidx.annotation.DrawableRes

data class Pokemon(
    val imageId: Int,
    @DrawableRes var backImage: Int
)
