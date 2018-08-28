package cgy.memoriz.data

import java.io.Serializable

open class QuizData : Serializable {

    var id : Int? = null
    var question : String? = null
    var answer : String? = null

    constructor()

    constructor(id : Int ?, question : String?, answer : String?) {
        this.id = id
        this.question = question
        this.answer = answer
    }
}