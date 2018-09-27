package cgy.memoriz.fragment.chat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.adapter.ChatHistoryAdapter
import cgy.memoriz.adapter.ChatHistoryInterface
import cgy.memoriz.data.ChatHistoryData
import cgy.memoriz.data.ChatHistoryObject
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.EventBus
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_chat.view.*

class Chat : MainActivityBaseFragment(), ChatHistoryInterface {

    private lateinit var recycleView: RecyclerView
    private lateinit var recycleAdapter: ChatHistoryAdapter
    private val firebaseChat = FirebaseChat()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        recycleView = view.rv_recent_chat
        setTitle("Chat")

        /*Use SharePref get back your current acount id.
         *Use SharePref to get back your current account user type. If not lecturer , make the button add group gone.
         * Eg: view.btn_add_group.visibility=GONE
         */
        showProgressDialog()
        firebaseChat.getChatHistory(SharedPref.userID)

        view.btn_add.setOnClickListener {
            switchFragment(ShowAvailableUser())
        }

        if (SharedPref.userType == "Lecturer") {
            view.btn_add_group.setOnClickListener {
                switchFragment(AddGroupFragment())
            }
        }
        else {
            view.btn_add_group.visibility = GONE
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        EventBus().registerOnBus(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus().unregisterFromBus(this)
    }

    override fun onClick(chat: ChatHistoryData) {
        switchFragment(ChatRoom().newInstance(chat))
    }

    private fun setRecycleView(data: ArrayList<ChatHistoryData>) {
        try {
            recycleAdapter = ChatHistoryAdapter(context!!, data, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("History Adapter error:", e.toString())
        }
    }

    @Subscribe
    fun ResponseFromFirebase(chatHistoryObject: ChatHistoryObject) {
        setRecycleView(chatHistoryObject.list)
        hideProgressDialog()
    }
}