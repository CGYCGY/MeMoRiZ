package cgy.memoriz.data

import java.io.Serializable

open class SetData : Serializable {

    var id : Int? = null
    var name : String? = null
    var type : String? = null
    var size : Int? = null
    lateinit var quizList : List<QuizData>
    lateinit var slideList : List<SlideData>

    constructor()

    constructor(id : Int ?, name : String?, size : Int?) {
        this.id = id
        this.name = name
        this.size = size
    }

    constructor(id : Int ?, name : String?, type : String?, size : Int?, quizList : List<QuizData>) {
        this.id = id
        this.name = name
        this.type = type
        this.size = size
        this.quizList = quizList
    }
}