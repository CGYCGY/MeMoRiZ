package cgy.memoriz.data

import java.io.Serializable

open class QuestionData : Serializable {

    var id : Int? = null
    var owner : String? = null
    var title : String? = null
    var body : String? = null
    var datetime : String? = null
    var condition : String? = null

    constructor()

    constructor(id : Int ?, title : String?, body : String?, datetime : String?, condition : String?) {
        this.id = id
        this.title = title
        this.body = body
        this.datetime = datetime
        this.condition = condition
    }

    constructor(id : Int ?, owner : String?, title : String?, body : String?, datetime : String?, condition : String?) {
        this.id = id
        this.owner = owner
        this.title = title
        this.body = body
        this.datetime = datetime
        this.condition = condition
    }
}