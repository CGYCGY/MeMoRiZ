package cgy.memoriz.fragment.chat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.adapter.GroupAdapter
import cgy.memoriz.adapter.GroupAdapterInterface
import cgy.memoriz.adapter.UserAdapter
import cgy.memoriz.adapter.UserAdapterInterface
import cgy.memoriz.data.GroupChatData
import cgy.memoriz.data.UserData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.EventBus
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_show_available_user.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ShowAvailableUser : MainActivityBaseFragment(), UserAdapterInterface, GroupAdapterInterface {

    private lateinit var recycleView: RecyclerView
    private lateinit var recycleAdapter: UserAdapter
    private lateinit var groupAdapter: GroupAdapter
    private val studentList = ArrayList<UserData>()
    private val lecturerList = ArrayList<UserData>()

    private var firebase = FirebaseChat()

    override fun onGroupClick(group: GroupChatData) {
        switchFragment(ChatRoom().newInstance(group))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_show_available_user, container, false)

        setTitle("Available User")
        recycleView = view.rv_user

        studentList.clear()
        lecturerList.clear()

        loadUserList()

        view.btn_student.setOnClickListener{
            setRecycleView(studentList)
        }

        view.btn_lecturer.setOnClickListener{
            setRecycleView(lecturerList)
        }
        /*The group chat  i use firebase store,  so no need use your mysql database to store. */
        view.btn_classroom.setOnClickListener{
            firebase.getAvailableGroupChat()
        }

        setRecycleView(lecturerList)
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
        } catch (e : NullPointerException) {
            Log.d("ShowAvailableUser error", e.toString())
        }
    }

    private fun setGroupRecycleView(data: ArrayList<GroupChatData>) {
        try {
            groupAdapter = GroupAdapter(context!!, data, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = groupAdapter
        } catch (e : NullPointerException) {
            Log.d("ShowAvailableUser error", e.toString())
        }
    }

    @Subscribe
    fun RespondFromFirebase(arrayList: ArrayList<GroupChatData>){
        setGroupRecycleView(arrayList)
    }

//    @Subscribe
//    fun RespondFromFirebase(arrayList: ArrayList<UserData>){
//        setRecycleView(arrayList)
//    }

    private fun loadUserList() {
        val stringRequest = object : StringRequest(Request.Method.GET, URLEndpoint.urlGetUserList,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)

                        val tableIsBlank = obj.getString("table").isBlank()
                        Log.d("table blank mou", tableIsBlank.toString())
                        if (tableIsBlank) {
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                            jsonToArrayList(obj.getJSONArray("table"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {

        for (i in 0 until obj.length())
            if (obj.getJSONObject(i).getString("u_type") == "Student" && obj.getJSONObject(i).getString("u_email") != SharedPref.userEmail) {
                studentList.add(UserData(
                        obj.getJSONObject(i).getInt("u_id"),
                        obj.getJSONObject(i).getString("u_email"),
                        obj.getJSONObject(i).getString("u_name"),
                        obj.getJSONObject(i).getString("u_type")))
            }
            else if (obj.getJSONObject(i).getString("u_type") == "Lecturer" && obj.getJSONObject(i).getString("u_email") != SharedPref.userEmail) {
                lecturerList.add(UserData(
                        obj.getJSONObject(i).getInt("u_id"),
                        obj.getJSONObject(i).getString("u_email"),
                        obj.getJSONObject(i).getString("u_name"),
                        obj.getJSONObject(i).getString("u_type")))
            }
    }
}