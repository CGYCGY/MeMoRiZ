package cgy.memoriz.fragment.chat

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.data.UserData
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
                firebase.saveGroupChat(view.et_name.text.toString(),"sender_id")
                showToastMessage("Successfully create group.")
            }
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

    @Subscribe
    fun RespondFromFirebase(user: UserData) {
        //Pop back this fragment before move to next fragment.
        switchFragmentWithNonBackStack(ChatRoom().newInstance(user))
    }

}