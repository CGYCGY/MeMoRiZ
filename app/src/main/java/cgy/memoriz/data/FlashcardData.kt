package cgy.memoriz.data

import java.io.Serializable

open class FlashcardData : Serializable {

    var id : Int? = null
    var selection : String? = null
    var card1 : String? = null
    var card2 : String? = null

    constructor()

    constructor(id : Int ?, selection : String?, card1 : String?, card2 : String?) {
        this.id = id
        this.selection = selection
        this.card1 = card1
        this.card2 = card2
    }
}
