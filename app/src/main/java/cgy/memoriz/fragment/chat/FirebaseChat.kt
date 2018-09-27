package cgy.memoriz.fragment.chat

import cgy.memoriz.data.*
import cgy.memoriz.others.EventBus
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat.getTimeInstance
import java.util.*

class FirebaseChat {

    private val LOG_TAG = "FirebaseChat"

//    Save the group chat to Firebase
    fun saveGroupChat(groupName: String,senderId: String) {
        val groupDb = FirebaseDatabase.getInstance().getReference("GroupChat")
        val chatKey = groupDb.push().key
//        Save record to recent chat
        val db = FirebaseDatabase.getInstance().getReference("ChatRecord").child(senderId).child(chatKey!!)
//        For group chat , the sender ID dont have any use
        db.setValue(ChatHistoryData(chatKey, senderId, groupName))
        groupDb.child(chatKey).setValue(GroupChatData(chatKey, groupName))
        EventBus().post(UserData(chatKey, groupName))
    }

//    Get created Group from Firebase
    fun getAvailableGroupChat() {
        val groupDb = FirebaseDatabase.getInstance().getReference("GroupChat")
        groupDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val arrayContainer = ArrayList<GroupChatData>()
                for (ds in p0.children) {
                    val history = ds.getValue(GroupChatData::class.java)!!
                    arrayContainer.add(history)
                }
                EventBus().post(arrayContainer)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

//    Check for existing chat key if the user want add people from the list and it selected people whose chat before
//     it will show previous conversation if it exist
    fun checkExistingChatKey(senderID : String, receiverID : String) {
        val db = FirebaseDatabase.getInstance().getReference("ChatRecord").child(senderID).child(receiverID)
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val existingKey = p0.getValue(ChatHistoryData::class.java)
                if (existingKey?.chatKey != null) {
                    EventBus().post(existingKey.chatKey)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    fun saveChatHistory(senderID : String, senderName : String, receiverID : String, receiverName : String,
                        msg : String, chatKey : String?, groupChatKey : String?) {
        val db = FirebaseDatabase.getInstance().getReference("ChatRecord").child(senderID).child(receiverID)
        val db1 = FirebaseDatabase.getInstance().getReference("ChatRecord").child(receiverID).child(senderID)
//        Chat key not null mean chat before
//        Chat key null mean did not chat before between the sender and receiver
        if (chatKey != null) {
            val detailDB = FirebaseDatabase.getInstance().getReference("ChatRecordDetail").child(chatKey)
            val messageID = detailDB.push().key
            saveChatDetails(chatKey, MessageData(messageID!!, senderName, msg, getCurrentTime(), senderID), true)
        } else {
            val detailDB = FirebaseDatabase.getInstance().getReference("ChatRecordDetail")
            var chatKey : String? = null
            if (groupChatKey != null) {
                chatKey = groupChatKey
            }
            else {
                chatKey = detailDB.push().key
            }
            val messageID = detailDB.push().key
//            Save recent chat to both sender and receiver
            db.setValue(ChatHistoryData(chatKey!!, receiverID, receiverName))
            db1.setValue(ChatHistoryData(chatKey!!, senderID, senderName))

            saveChatDetails(chatKey, MessageData(messageID!!, senderName, msg, getCurrentTime(), senderID), false)
        }
    }

    fun saveChatDetails(id: String, message: MessageData, hasChatKey: Boolean) {
        val db = FirebaseDatabase.getInstance().getReference("ChatRecordDetail")
        val key = db.push().key!!
        db.child(id).child(key).setValue(message)
        if (!hasChatKey) {
            EventBus().post(id)
        }
    }

    fun getChatHistory(id : String) {
        val db = FirebaseDatabase.getInstance().getReference("ChatRecord").child(id)
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                val arrayContainer = ArrayList<ChatHistoryData>()
                for (ds in dataSnapshot.children) {
                    val history = ds.getValue(ChatHistoryData::class.java)!!
                    arrayContainer.add(history)
                }
                EventBus().post(ChatHistoryObject(arrayContainer))
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun getChatDetails(id: String) {
        val db = FirebaseDatabase.getInstance().getReference("ChatRecordDetail").child(id)
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val arrayContainer = ArrayList<MessageData>()
                for (ds in dataSnapshot.children) {
                    val history = ds.getValue(MessageData::class.java)!!
                    arrayContainer.add(history)
                }
                EventBus().post(MessageObject(arrayContainer))
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

//    Can try other format if u want
    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur")
        val dateformat = getTimeInstance()
        val time = dateformat.format(calendar.time)
        return time
    }

}