package cgy.memoriz.data

import java.io.Serializable

open class ReportData : Serializable {

    var id : Int? = null
    var title : String? = null
    var type : String? = null
    var body : String? = null
    var datetime : String? = null
    var status : String? = null

    constructor()

    constructor(id : Int ?, title : String?, type : String?, body : String?, datetime : String?, status : String?) {
        this.id = id
        this.title = title
        this.type = type
        this.body = body
        this.datetime = datetime
        this.status = status
    }
}