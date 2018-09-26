package cgy.memoriz.data

import java.io.Serializable

open class UserData : Serializable {
    var id : Int? = null
    var email : String? = null
    var name : String? = null
    var type : String? = null
    var address : String? = null
    var experience : String? = null
    var picture : String? = null

    constructor(id: Int?, email: String?, name: String?, type: String?) {
        this.id = id
        this.email = email
        this.name = name
        this.type = type
    }

    constructor(id: Int?, name : String?, type: String?) {
        this.id = id
        this.name = name
        this.type = type
    }

    constructor(email: String?, name: String?, type: String?) {
        this.email = email
        this.name = name
        this.type = type
    }

    constructor(email: String?, name: String?) {
        this.email = email
        this.name = name
    }
}