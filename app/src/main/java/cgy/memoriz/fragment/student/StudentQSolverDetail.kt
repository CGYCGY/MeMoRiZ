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
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.adapter.QSolverAnswerAdapter
import cgy.memoriz.adapter.QSolverAnswerAdapterInterface
import cgy.memoriz.data.AnswerData
import cgy.memoriz.data.QuestionData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.DialogFactory
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.base_list_qsolver.view.*
import kotlinx.android.synthetic.main.fragment_qsolver_detail.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StudentQSolverDetail : MainActivityBaseFragment(), QSolverAnswerAdapterInterface {
    private lateinit var recycleAdapter: QSolverAnswerAdapter
    private lateinit var recycleView: RecyclerView

    private var dialogFactory = DialogFactory()

    fun newInstance(question: QuestionData): StudentQSolverDetail{
        val args = Bundle()
        args.putSerializable("question detail", question)
        val fragment = StudentQSolverDetail()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(answer: AnswerData) {
        Log.d("CLICKED HERE YOUR DATA", answer.body)
        dialogFactory.createOneButtonDialog(context!!, "Answer", answer.body.toString(),
                "back").show()
    }

    override fun onLongClick(answer: AnswerData) {
        Log.d("LONG CLICKED! YOUR DATA", answer.body)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_qsolver_detail, container, false)
        recycleView = view.qSolverDetail
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val question : QuestionData = bundle.getSerializable("question detail") as QuestionData

            view.qsolver_title.text = question.title
            view.qsolver_body.text = question.body
            view.qsolver_owner.text = question.owner
            view.qsolver_datetime.text = question.datetime
            view.qsolver_condition.text = question.condition

            setTitle("Question Answer List")

            loadAnswerList(question)

            val longtext = "Title: " + question.title +"\nBody: " + question.body

            view.qsolver_base.setOnClickListener {
                dialogFactory.createOneButtonDialog(context!!, "Question", longtext,
                        "back").show()
            }

            view.submitAnswerBtn.setOnClickListener{
                switchFragment(SubmitAnswer().newInstance(question))
            }
        }else {
            Log.e("missing QuestionData", "StudentQSolverDetail got error!")
        }

        return view
    }

    private fun setRecycleView(answerList: ArrayList<AnswerData>) {
        try {
            recycleAdapter = QSolverAnswerAdapter(context!!, answerList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("QSolver Adapter error:", e.toString())
        }
    }

    private fun loadAnswerList(question: QuestionData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetAnswer,
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

                Log.d("question id", question.id.toString())
                params["qstn_id"] = question.id.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<AnswerData>()

        for (i in 0 until obj.length())
            list.add(AnswerData(
                    obj.getJSONObject(i).getString("ans_owner"),
                    obj.getJSONObject(i).getString("ans_body"),
                    obj.getJSONObject(i).getString("ans_datetime")))

        setRecycleView(list)
    }
}