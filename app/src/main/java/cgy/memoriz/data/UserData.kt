package cgy.memoriz.data

import java.io.Serializable

open class UserData : Serializable {
    var id: Int? = null
    var name: String? = null
    var type: String? = null

    constructor(id: Int?, name: String?, type: String?) {
        this.id = id
        this.name = name
        this.type = type
    }
}