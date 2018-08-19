package cgy.memoriz.adapter

import cgy.memoriz.data.QuestionData

interface QHelperAdapterInterface {

    fun onClick(question : QuestionData)

    fun onLongClick(question : QuestionData)
}