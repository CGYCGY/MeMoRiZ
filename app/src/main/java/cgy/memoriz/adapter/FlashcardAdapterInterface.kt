package cgy.memoriz.adapter

import cgy.memoriz.data.FlashcardData

interface FlashcardAdapterInterface {

    fun onClick(flashcard : FlashcardData)

    fun onLongClick(flashcard : FlashcardData)
}