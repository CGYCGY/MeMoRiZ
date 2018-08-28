package cgy.memoriz.adapter

import cgy.memoriz.data.SetData

interface QuizSetAdapterInterface {

    fun onClick(quiz : SetData)

    fun onLongClick(quiz : SetData)
}