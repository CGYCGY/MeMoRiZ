package cgy.memoriz.adapter

import cgy.memoriz.data.QuizData

interface QuizMcqAdapterInterface {

    fun onCheck(quiz : QuizData, answer : String)
}