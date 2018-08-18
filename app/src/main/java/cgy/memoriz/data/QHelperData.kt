package cgy.memoriz.data

import java.io.Serializable

open class QHelperData : Serializable {

    var title : String? = null
    var body : String? = null
    var datetime : String? = null
    var condition : String? = null

    constructor()

    constructor(title : String ?, body : String?, datetime : String?, condition : String?) {
        this.title = title
        this.body = body
        this.datetime = datetime
        this.condition = condition
    }
}