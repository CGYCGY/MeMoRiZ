package cgy.memoriz.data

import java.io.Serializable

open class QuizData : Serializable {

    var id : Int? = null
    var question : String? = null
    var answer : String? = null
    var choices = ArrayList<String>()
    var studentSelection : Int = 777

    constructor()

    constructor(id : Int ?, question : String?, answer : String?) {
        this.id = id
        this.question = question
        this.answer = answer
    }

    constructor(id : Int ?, question : String?, answer : String?, choice : String?) {
        this.id = id
        this.question = question
        this.answer = answer
        this.choices.add(choice!!)
    }
}