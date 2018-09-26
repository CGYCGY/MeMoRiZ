package cgy.memoriz.fragment.lecturer

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
import cgy.memoriz.adapter.ClassListAdapter
import cgy.memoriz.adapter.ClassListAdapterInterface
import cgy.memoriz.data.ClassData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.fragment.mainmenu.second.MainMenu2
import cgy.memoriz.others.DialogFactory
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_lecturer_class.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LecturerClassList : MainActivityBaseFragment(), ClassListAdapterInterface {
    private lateinit var recycleAdapter: ClassListAdapter
    private lateinit var recycleView: RecyclerView

    private var textGet : String ?= null
    private var dialogFactory = DialogFactory()

    fun newInstance(text : String) : LecturerClassList {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = LecturerClassList()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(classInfo: ClassData) {
        Log.d("CLICKED HERE YOUR DATA", classInfo.name)
        switchFragment(MainMenu2().newInstance(classInfo, getString(R.string.SMManager)))
    }

    override fun onLongClick(classInfo: ClassData) {
        Log.d("LONG CLICKED! YOUR DATA", classInfo.name)
        dialogFactory.createOneButtonDialog(context!!, "Class Name", classInfo.name.toString(),
                "back").show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lecturer_class, container, false)
        recycleView = view.lecturer_class
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getString("value1")
            setTitle("$textGet")
        }else {
            setTitle("Class List")
        }

        loadClassList()

        view.createClassBtn.setOnClickListener {
            switchFragment(CreateClass())
        }

        return view
    }

    private fun setRecycleView(classList: ArrayList<ClassData>) {
        try {
            recycleAdapter = ClassListAdapter(context!!, classList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("Class Adapter error:", e.toString())
        }
    }

    private fun loadClassList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetClassList,
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

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["u_email"] = SharedPref.userEmail
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<ClassData>()

        for (i in 0 until obj.length())
            list.add(ClassData(
                    obj.getJSONObject(i).getInt("cls_id"),
                    obj.getJSONObject(i).getString("cls_name"),
                    obj.getJSONObject(i).getInt("cls_size")))

        setRecycleView(list)
    }
}