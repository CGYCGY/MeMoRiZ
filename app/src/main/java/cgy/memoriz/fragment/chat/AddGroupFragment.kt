package cgy.memoriz.fragment.chat

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.data.GroupChatData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.EventBus
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_add_group.view.*

class AddGroupFragment : MainActivityBaseFragment() {

    private var firebase=  FirebaseChat()
    private val LOG_TAG = "Add Group Fragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_group, container, false)

        view.btn_next.setOnClickListener{
            if(!TextUtils.isEmpty(view.et_name.text.toString())){
                /*If want dialog(yes/no) for user to confirm whether want create group
                 , can just use dialog factory call createTwoButtonDialog.*/
                firebase.saveGroupChat(view.et_name.text.toString(), SharedPref.userEmail)
                showToastMessage("Successfully create group.")
            }
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

    @Subscribe
    fun RespondFromFirebase(group: GroupChatData) {
        //Pop back this fragment before move to next fragment.
        switchFragmentWithNonBackStack(ChatRoom().newInstance(group))
    }

}