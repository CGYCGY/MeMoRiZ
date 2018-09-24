package cgy.memoriz.fragment.student

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
import cgy.memoriz.adapter.FCSetAdapter
import cgy.memoriz.adapter.FCSetAdapterInterface
import cgy.memoriz.data.FCSetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_student_flashcardset.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StudentFCSetList : MainActivityBaseFragment(), FCSetAdapterInterface {
    private lateinit var recycleAdapter: FCSetAdapter
    private lateinit var recycleView: RecyclerView

    private var textGet : String ?= null

    fun newInstance(text : String) : StudentFCSetList {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = StudentFCSetList()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(fcSetInfo : FCSetData) {
        Log.d("CLICKED HERE YOUR DATA", fcSetInfo.name)
        switchFragment(StudentFCSetDetail().newInstance(fcSetInfo))
    }

    override fun onLongClick(fcSetInfo : FCSetData) {
        Log.d("LONG CLICKED! YOUR DATA", fcSetInfo.name)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_flashcardset, container, false)
        recycleView = view.student_fcset
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getString("value1")
            setTitle("$textGet")
        }else {
            setTitle("Flashcard Set List")
        }

        loadFCSetList()

        view.createFCSetBtn.setOnClickListener {
            switchFragment(CreateFCSet())
        }

        return view
    }

    private fun setRecycleView(fcSetList: ArrayList<FCSetData>) {
        try {
            recycleAdapter = FCSetAdapter(context!!, fcSetList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("FCSet Adapter error:", e.toString())
        }
    }

    private fun loadFCSetList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetFCSet,
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
        val list = ArrayList<FCSetData>()

        for (i in 0 until obj.length())
            list.add(FCSetData(
                    obj.getJSONObject(i).getInt("fcs_id"),
                    obj.getJSONObject(i).getString("fcs_name"),
                    obj.getJSONObject(i).getInt("fcs_size")))

        setRecycleView(list)
    }
}