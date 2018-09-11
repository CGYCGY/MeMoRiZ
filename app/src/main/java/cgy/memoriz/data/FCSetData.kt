package cgy.memoriz.data

import java.io.Serializable

open class FCSetData : Serializable {

    var id : Int? = null
    var name : String? = null
    var size : Int? = null
    lateinit var fcList : List<FlashcardData>

    constructor()

    constructor(id : Int ?, name : String?, size : Int?) {
        this.id = id
        this.name = name
        this.size = size
    }

    constructor(id : Int ?, name : String?, size : Int?, fcList : List<FlashcardData>) {
        this.id = id
        this.name = name
        this.size = size
        this.fcList = fcList
    }
}