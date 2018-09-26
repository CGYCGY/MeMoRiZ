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
import cgy.memoriz.adapter.QuizAdapter
import cgy.memoriz.adapter.QuizAdapterInterface
import cgy.memoriz.data.QuizData
import cgy.memoriz.data.SetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.DialogFactory
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.base_list_quiz_set.view.*
import kotlinx.android.synthetic.main.fragment_lecturer_quiz.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LecturerQuiz : MainActivityBaseFragment(), QuizAdapterInterface {
    private lateinit var recycleAdapter: QuizAdapter
    private lateinit var recycleView: RecyclerView

    private var quiz = SetData()
    private var dialogFactory = DialogFactory()

    fun newInstance(quiz: SetData): LecturerQuiz{
        val args = Bundle()
        args.putSerializable("quiz", quiz)
        val fragment = LecturerQuiz()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(quiz: QuizData) {
        Log.d("CLICKED HERE YOUR DATA", quiz.question)
        switchFragment(LecturerQuizDetail().newInstance(quiz, this.quiz.id!!))
    }

    override fun onLongClick(quiz: QuizData) {
        Log.d("LONG CLICKED! YOUR DATA", quiz.question)
        val quizDetail = "Quiz Question: " + quiz.question + " \nQuiz Answer: " + quiz.answer
        dialogFactory.createOneButtonDialog(context!!, "Quiz Detail", quizDetail,
                "back").show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lecturer_quiz, container, false)
        recycleView = view.quiz_list
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            quiz  = bundle.getSerializable("quiz") as SetData

            view.quiz_set_name.text = quiz.name
            view.quiz_set_size.text = quiz.size.toString()

            setTitle("Quiz Answer List")

            loadQuizList(quiz)

            view.quiz_set_base.setOnClickListener {
                dialogFactory.createOneButtonDialog(context!!, "Quiz Set Name", quiz.name.toString(),
                        "back").show()
            }

            view.addQuizBtn.setOnClickListener{
                switchFragment(AddQuiz().newInstance(quiz))
            }
        }else {
            Log.e("missing QuizData", "LecturerQuiz got error!")
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        loadQuizList(quiz)
    }

    private fun setRecycleView(quizList: ArrayList<QuizData>) {
        try {
            recycleAdapter = QuizAdapter(context!!, quizList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("Quiz Adapter error:", e.toString())
        }
    }

    private fun loadQuizList(quiz: SetData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetQuiz,
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
                        view!!.quiz_set_size.text = quiz.size.toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["set_id"] = quiz.id.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<QuizData>()

        for (i in 0 until obj.length())
            list.add(QuizData(
                    obj.getJSONObject(i).getInt("qz_id"),
                    obj.getJSONObject(i).getString("qz_qstn"),
                    obj.getJSONObject(i).getString("qz_ans")))

        quiz.size = list.size
        setRecycleView(list)
    }
}