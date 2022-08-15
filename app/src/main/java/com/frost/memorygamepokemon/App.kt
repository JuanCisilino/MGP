package com.frost.memorygamepokemon

import android.app.Application

class App: Application() {

    companion object {
        const val EASY_DIFFICULT = 0
        const val EASY_COLUMNS = 4
        const val EASY_TOTAL_BLOCKS = 16

        const val MEDIUM_DIFFICULT = 1
        const val MEDIUM_COLUMNS = 4
        const val MEDIUM_TOTAL_BLOCKS = 20

        const val HARD_DIFFICULT = 2
        const val HARD_COLUMNS = 6
        const val HARD_TOTAL_BLOCKS = 30
    }

}