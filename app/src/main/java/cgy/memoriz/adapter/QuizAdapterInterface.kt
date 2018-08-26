package cgy.memoriz.adapter

import cgy.memoriz.data.QuizData

interface QuizAdapterInterface {

    fun onClick(quiz : QuizData)

    fun onLongClick(quiz : QuizData)
}