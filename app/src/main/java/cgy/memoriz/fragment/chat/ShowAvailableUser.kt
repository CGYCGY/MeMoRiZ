package cgy.memoriz.fragment.chat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.adapter.UserAdapter
import cgy.memoriz.adapter.UserAdapterInterface
import cgy.memoriz.data.UserData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.EventBus
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_show_available_user.view.*

class ShowAvailableUser : MainActivityBaseFragment(), UserAdapterInterface {

    private lateinit var recycleView: RecyclerView
    private lateinit var recycleAdapter: UserAdapter

    private var firebase = FirebaseChat()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_show_available_user, container, false)

        setTitle("Available User")
        recycleView = view.rv_user

        /*Get all your user in your database .*/
        val listStudent = ArrayList<UserData>()
        listStudent.add(UserData(1, "ALI", "student"))
        listStudent.add(UserData(2, "ABU", "student"))
        listStudent.add(UserData(3, "AHMAD", "student"))

        val listLecturer= ArrayList<UserData>()
        listLecturer.add(UserData(4, "Akow", "Lecturer"))
        listLecturer.add(UserData(5, "Abeng", "Lecturer"))

        view.btn_student.setOnClickListener{
            setRecycleView(listStudent)
        }

        view.btn_lecturer.setOnClickListener{
            setRecycleView(listLecturer)
        }
        /*The group chat  i use firebase store,  so no need use your mysql database to store. */
        view.btn_classroom.setOnClickListener{
            firebase.getAvailableGroupChat()
        }

        setRecycleView(listLecturer)
        return view
    }

    override fun onClick(user: UserData) {
        switchFragment(ChatRoom().newInstance(user))
    }

    override fun onStart() {
        super.onStart()
        EventBus().registerOnBus(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus().unregisterFromBus(this)
    }
    private fun setRecycleView(data: ArrayList<UserData>) {
        try {
            recycleAdapter = UserAdapter(context!!, data, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("History Adapter error:", e.toString())
        }
    }

    @Subscribe
    fun RespondFromFirebase(arrayList: ArrayList<UserData>){
        setRecycleView(arrayList)
    }
}