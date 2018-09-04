package cgy.memoriz.data

import java.io.Serializable

open class ClassData : Serializable {

    var id : Int? = null
    var ownerID : Int? = null
    var owner : String? = null
    var name : String? = null
    var size : Int? = null

    constructor()

    constructor(id : Int ?, name : String?, size : Int?) {
        this.id = id
        this.name = name
        this.size = size
    }

    constructor(id : Int ?, owner : String?, name : String?, size : Int?) {
        this.id = id
        this.owner = owner
        this.name = name
        this.size = size
    }

    constructor(id : Int ?, ownerID : Int?, owner : String?, name : String?, size : Int?) {
        this.id = id
        this.ownerID = ownerID
        this.owner = owner
        this.name = name
        this.size = size
    }
}