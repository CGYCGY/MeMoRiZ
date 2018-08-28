package cgy.memoriz.data

import java.io.Serializable

open class ChatHistoryData : Serializable {

    var chatKey : String? = null
    var receiverID : String? = null
    var receiverName : String? = null

    constructor()

    constructor(chatKey : String?, receiverID : String?, receiverName : String?){
        this.chatKey = chatKey
        this.receiverID=receiverID
        this.receiverName= receiverName
    }
}

class ChatHistoryObject(list : ArrayList<ChatHistoryData>){

    var list : ArrayList<ChatHistoryData>

    init {
        this.list = list
    }
}