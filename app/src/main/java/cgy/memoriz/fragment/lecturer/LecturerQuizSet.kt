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
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.adapter.QuizSetAdapter
import cgy.memoriz.adapter.QuizSetAdapterInterface
import cgy.memoriz.data.ClassData
import cgy.memoriz.data.SetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_lecturer_quizset.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LecturerQuizSet : MainActivityBaseFragment(),QuizSetAdapterInterface {
    private lateinit var recycleAdapter: QuizSetAdapter
    private lateinit var recycleView: RecyclerView

    private lateinit var classInfo : ClassData

    fun newInstance(text : String) : LecturerQuizSet {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = LecturerQuizSet()
        fragment.arguments = args
        return fragment
    }

    fun newInstance(classInfo : ClassData): LecturerQuizSet{
        val args = Bundle()
        args.putSerializable("class info", classInfo)
        val fragment = LecturerQuizSet()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(quiz: SetData) {
        Log.d("CLICKED HERE YOUR DATA", quiz.name)
        switchFragment(LecturerQuiz().newInstance(quiz))
    }

    override fun onLongClick(quiz: SetData) {
        Log.d("LONG CLICKED! YOUR DATA", quiz.name)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lecturer_quizset, container, false)
        recycleView = view.lecturer_quiz_set
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            classInfo = bundle.getSerializable("class info") as ClassData
            setTitle("Quiz Set List")
        }else {
            setTitle("Quiz Set List")
        }

        loadQuizList()

        view.createQuizSetBtn.setOnClickListener {
            switchFragment(CreateQuizSet())
        }

        return view
    }

    private fun setRecycleView(quizList: ArrayList<SetData>) {
        try {
            recycleAdapter = QuizSetAdapter(context!!, quizList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("QHelper Adapter error:", e.toString())
        }
    }

    private fun loadQuizList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetSet,
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

                params["cls_id"] = classInfo.id.toString()
                params["set_type"] = "Quiz"

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<SetData>()

        for (i in 0 until obj.length())
            list.add(SetData(
                    obj.getJSONObject(i).getInt("set_id"),
                    obj.getJSONObject(i).getString("set_name"),
                    obj.getJSONObject(i).getInt("set_size")))

        setRecycleView(list)
    }
}