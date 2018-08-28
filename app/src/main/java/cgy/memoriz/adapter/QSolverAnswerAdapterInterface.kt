package cgy.memoriz.adapter

import cgy.memoriz.data.AnswerData

interface QSolverAnswerAdapterInterface {

    fun onClick(answer : AnswerData)

    fun onLongClick(answer : AnswerData)
}