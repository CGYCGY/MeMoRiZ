package cgy.memoriz.data

import java.io.Serializable

open class GroupData : Serializable {
    var id: String? = null
    var name: String? = null

    constructor(id: String?, name: String?) {
        this.id = id
        this.name = name
    }
}