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

    /* Save the group chat to Firebase.*/
    fun saveGroupChat(groupName: String,senderId: String) {
        val groupDb = FirebaseDatabase.getInstance().getReference("GroupChat")
        val chatKey = groupDb.push().key
        /*Save record to recent chat.*/
        val db = FirebaseDatabase.getInstance().getReference("ChatHistory").child(senderId).child(chatKey!!)
        /*For group chat , the sender ID perform no function.*/
        db.setValue(ChatHistoryData(chatKey, senderId, groupName))
        groupDb.child(chatKey).setValue(GroupChatData(chatKey, groupName))
        EventBus().post(UserData(chatKey, groupName))
    }

    /*Get created Group from Firebase.*/
    fun getAvailableGroupChat() {
        val groupDb = FirebaseDatabase.getInstance().getReference("GroupChat")
        groupDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                /*Changing arraylist of GroupChat to arraylist of User.
                 *So , can using same adapter to set up recycle view.
                 */
                val arrayContainer = ArrayList<UserData>()
                for (ds in p0.children) {
                    val history = ds.getValue(GroupChatData::class.java)!!
                    /* Not sure whether your User class got how many attribute,
                     * I think can reeuse it, those other parameter just put  left it empty.
                     * I currently need group id and group name -> change to User class.*/
                    arrayContainer.add(UserData(history.groupID!!, history.groupName!!))
                }
                EventBus().post(arrayContainer)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

    /*Check for existing chat key if the user want add people from the list and it selected people whose chat before,
     it will show previous conversation if it exist*/
    fun checkExistingChatKey(senderID : String, receiverID : String) {
        val db = FirebaseDatabase.getInstance().getReference("ChatHistory").child(senderID).child(receiverID)
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
                        msg : String, chatKey : String?) {
        val db = FirebaseDatabase.getInstance().getReference("ChatHistory").child(senderID).child(receiverID)
        val db1 = FirebaseDatabase.getInstance().getReference("ChatHistory").child(receiverID).child(senderID)
        /*Chat key not null mean chat before.
         *Chat key null mean did not chat before between the sender and receiver.  */
        if (chatKey != null) {
            val detailDB = FirebaseDatabase.getInstance().getReference("ChatDetailHistory").child(chatKey)
            val messageID = detailDB.push().key
            saveChatDetails(chatKey, MessageData(messageID!!, senderName, msg, getCurrentTime(), senderID), true)
        } else {
            val detailDB = FirebaseDatabase.getInstance().getReference("ChatDetailHistory")
            val chatKey = detailDB.push().key
            val messageID = detailDB.push().key
            /*Save recent chat to both sender and receiver*/
            db.setValue(ChatHistoryData(chatKey!!, receiverID, receiverName))
            db1.setValue(ChatHistoryData(chatKey!!, senderID, senderName))
            /*-----------------------------------------------------------*/
            saveChatDetails(chatKey, MessageData(messageID!!, senderName, msg, getCurrentTime(), senderID), false)
        }
    }

    fun saveChatDetails(id: String, message: MessageData, hasChatKey: Boolean) {
        val db = FirebaseDatabase.getInstance().getReference("ChatDetailHistory")
        val key = db.push().key!!
        db.child(id).child(key).setValue(message)
        if (!hasChatKey) {
            EventBus().post(id)
        }
    }

    fun getChatHistory(id : String) {
        val db = FirebaseDatabase.getInstance().getReference("ChatHistory").child(id)
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
        val db = FirebaseDatabase.getInstance().getReference("ChatDetailHistory").child(id)
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

    /*Can try other format if u want .*/
    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur")
        val dateformat = getTimeInstance()
        val time = dateformat.format(calendar.time)
        return time
    }

}