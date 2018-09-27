package cgy.memoriz.fragment.chat

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.adapter.ChatRoomAdapter
import cgy.memoriz.data.*
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.EventBus
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_chat_room.view.*

class   ChatRoom : MainActivityBaseFragment() {

    private lateinit var recycleView : RecyclerView
    private lateinit var recycleAdapter : ChatRoomAdapter
    private lateinit var receiverID : String
    private lateinit var receiverName : String

    private var receiver : UserData? = null
    private var groupChat : GroupChatData? = null
    private var chatHistory : ChatHistoryData? = null
    private val firebase = FirebaseChat()
    private var chatKey : String? = null
    private var groupChatKey : String? = null

    fun newInstance(user: UserData): ChatRoom {
        val args = Bundle()
        args.putSerializable("new user", user)
        val fragment = ChatRoom()
        fragment.arguments = args
        return fragment
    }

    fun newInstance(group: GroupChatData): ChatRoom {
        val args = Bundle()
        args.putSerializable("group", group)
        val fragment = ChatRoom()
        fragment.arguments = args
        return fragment
    }

    fun newInstance(chat: ChatHistoryData): ChatRoom {
        val args = Bundle()
        args.putSerializable("chat", chat)
        val fragment = ChatRoom()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat_room, container, false)
        recycleView = view.rv_chat
//        The user can come to this fragment by switch to recent chat
//         Or redirect to user / group list there .
        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey("new user")) {
                receiver = bundle.getSerializable("new user") as UserData
                receiverName = receiver!!.name.toString()
                receiverID = receiver!!.id.toString()
                firebase.checkExistingChatKey(SharedPref.userID, receiverID)
            }
            else if (bundle.containsKey("group")) {
                groupChat = bundle.getSerializable("group") as GroupChatData
                receiverName = groupChat!!.groupName.toString()
                receiverID = groupChat!!.groupID.toString()
                groupChatKey = receiverID
                firebase.checkExistingChatKey(SharedPref.userID, receiverID)
            }
            else {
                chatHistory = bundle.getSerializable("chat") as ChatHistoryData
                receiverID = chatHistory!!.receiverID
                receiverName = chatHistory!!.receiverName
                chatKey = chatHistory!!.chatKey
            }
            setTitle(receiverName)
        }
        view.btn_send.setOnClickListener {
            sendMessage()
        }
        if (chatKey != null) {
            firebase.getChatDetails(chatKey!!)
        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus().registerOnBus(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus().unregisterFromBus(this)
    }


    private fun sendMessage() {
        val messageToSend = view!!.et_message.text.toString()
        if (TextUtils.isEmpty(messageToSend)) {
            return
        } else {
            firebase.saveChatHistory(SharedPref.userID, SharedPref.userName,
                    receiverID, receiverName, messageToSend, chatKey, groupChatKey)
            view!!.et_message.setText("")
        }
    }

    private fun setRecycleView(data: ArrayList<MessageData>) {
        try {
            recycleAdapter = ChatRoomAdapter(context!!, data)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
            recycleView.scrollToPosition(data.size - 1)
        } catch (e: Exception) {
            Log.d("History Adapter error:", e.toString())
        }
    }

    @Subscribe
    fun ResponseFromFirebase(chatId: String) {
        chatKey = chatId
        firebase.getChatDetails(chatKey!!)
    }

    @Subscribe
    fun ResponseFromFirebase(messageObject: MessageObject) {
        setRecycleView(messageObject.messageList)
    }
}