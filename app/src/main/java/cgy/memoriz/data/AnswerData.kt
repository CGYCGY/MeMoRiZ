package cgy.memoriz.data

import java.io.Serializable

open class AnswerData : Serializable {

    var id : Int? = null
    var owner : String? = null
    var body : String? = null
    var datetime : String? = null
    var likecount : Int? = null
    var pin : Boolean? = null

    constructor()

    constructor(owner: String?, body : String?, datetime: String?) {
        this.owner = owner
        this.body = body
        this.datetime = datetime
    }

    constructor(id : Int ?, owner : String?, body : String?, datetime : String?, likecount : Int?, pin : Boolean?) {
        this.id = id
        this.owner = owner
        this.body = body
        this.datetime = datetime
        this.likecount = likecount
        this.pin = pin
    }
}