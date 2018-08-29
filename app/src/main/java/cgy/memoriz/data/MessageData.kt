package cgy.memoriz.data

import java.io.Serializable

open class MessageData : Serializable {

    var id : String? = null
    var name : String? = null
    var message : String? = null
    var time : String? = null
    var senderID : String? = null

    constructor()

    constructor(id: String?, name: String?, message: String?, time: String?, senderID: String?) {
        this.id = id
        this.name = name
        this.message = message
        this.time = time
        this.senderID = senderID
    }
}

class MessageObject {
    var messageList : ArrayList<MessageData>

    constructor(messageList: ArrayList<MessageData>) {
        this.messageList = messageList
    }
}