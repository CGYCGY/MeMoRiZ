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
import cgy.memoriz.adapter.QHelperAdapter
import cgy.memoriz.adapter.QHelperAdapterInterface
import cgy.memoriz.data.QuestionData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_student_qhelper.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StudentQHelper : MainActivityBaseFragment(),QHelperAdapterInterface {
    private lateinit var recycleAdapter: QHelperAdapter
    private lateinit var recycleView: RecyclerView

    private var textGet : String ?= null

    fun newInstance(text : String) : StudentQHelper {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = StudentQHelper()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(question: QuestionData) {
        Log.d("CLICKED HERE YOUR DATA", question.title)
        switchFragment(StudentQHelperDetail().newInstance(question))
    }

    override fun onLongClick(question: QuestionData) {
        Log.d("LONG CLICKED! YOUR DATA", question.title)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_qhelper, container, false)
        recycleView = view.qHelper
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getString("value1")
            setTitle("$textGet")
        }else {
            setTitle("Question Helper")
        }

        loadQuestionList()

        view.createQuestionBtn.setOnClickListener {
            switchFragment(CreateQuestion())
        }

        return view
    }

    private fun setRecycleView(questionList: ArrayList<QuestionData>) {
        try {
            recycleAdapter = QHelperAdapter(context!!, questionList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("QHelper Adapter error:", e.toString())
        }
    }

    private fun loadQuestionList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetQuestionHelper,
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
        val list = ArrayList<QuestionData>()

        for (i in 0 until obj.length())
            list.add(QuestionData(
                    obj.getJSONObject(i).getInt("qstn_id"),
                    obj.getJSONObject(i).getString("qstn_title"),
                    obj.getJSONObject(i).getString("qstn_body"),
                    obj.getJSONObject(i).getString("qstn_datetime"),
                    obj.getJSONObject(i).getString("qstn_cond")))

        setRecycleView(list)
    }
}