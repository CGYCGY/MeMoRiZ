package cgy.memoriz.fragment.admin

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
import cgy.memoriz.adapter.UserAdapter
import cgy.memoriz.adapter.UserAdapterInterface
import cgy.memoriz.data.UserData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.fragment.mainmenu.second.MainMenu2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_admin_uac.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AdminUAManager : MainActivityBaseFragment(), UserAdapterInterface {
    private lateinit var recycleAdapter: UserAdapter
    private lateinit var recycleView: RecyclerView

    private var textGet : String ?= null

    fun newInstance(text : String) : AdminUAManager {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = AdminUAManager()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(user : UserData) {
        Log.d("CLICKED HERE YOUR DATA", user.name)
        switchFragment(MainMenu2().newInstance(user, "User"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_uac, container, false)
        recycleView = view.admin_uac_list
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getString("value1")
            setTitle("$textGet")
        }else {
            setTitle("User Account Manager")
        }

        loadUserList()

        return view
    }

    private fun setRecycleView(userList: ArrayList<UserData>) {
        try {
            recycleAdapter = UserAdapter(context!!, userList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("User Adapter error:", e.toString())
        }
    }

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
        val list = ArrayList<UserData>()

        for (i in 0 until obj.length()) {
            if (SharedPref.userEmail != obj.getJSONObject(i).getString("u_email"))
                list.add(UserData(
                        obj.getJSONObject(i).getString("u_email"),
                        obj.getJSONObject(i).getString("u_name"),
                        obj.getJSONObject(i).getString("u_type")))
        }

        setRecycleView(list)
    }
}