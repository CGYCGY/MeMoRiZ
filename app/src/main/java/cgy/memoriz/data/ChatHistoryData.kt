package cgy.memoriz.data

import java.io.Serializable

open class ChatHistoryData : Serializable {

    lateinit var chatKey : String
    lateinit var receiverID : String
    lateinit var receiverName : String

    constructor()

    constructor(chatKey : String, receiverID : String, receiverName : String){
        this.chatKey = chatKey
        this.receiverID = receiverID
        this.receiverName = receiverName
    }
}

class ChatHistoryObject(list : ArrayList<ChatHistoryData>){

    var list : ArrayList<ChatHistoryData>

    init {
        this.list = list
    }
}