package cgy.memoriz.data

import java.io.Serializable

open class SlideData : Serializable {

    var id : Int? = null
    var content : String? = null

    constructor()

    constructor(id : Int ?, content : String?) {
        this.id = id
        this.content = content
    }
}