package cgy.memoriz.fragment.student

import android.os.Bundle
import android.os.CountDownTimer
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
import cgy.memoriz.adapter.QuizMcqAdapter
import cgy.memoriz.adapter.QuizMcqAdapterInterface
import cgy.memoriz.data.QuizData
import cgy.memoriz.data.SetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.filterString
import cgy.memoriz.others.shuffleString
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_student_quiz_mcq.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StudentStartQuiz : MainActivityBaseFragment(), QuizMcqAdapterInterface {
    private lateinit var recycleAdapter: QuizMcqAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var quizSetInfo : SetData

    private val list = ArrayList<QuizData>()
    private var stopCountDown = false

    fun newInstance(text : String) : StudentStartQuiz {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = StudentStartQuiz()
        fragment.arguments = args
        return fragment
    }

    fun newInstance(quiz : SetData): StudentStartQuiz{
        val args = Bundle()
        args.putSerializable("quiz set info", quiz)
        val fragment = StudentStartQuiz()
        fragment.arguments = args
        return fragment
    }

    override fun onCheck(quiz: QuizData, answer: String) {
        for (i in 0 until list.size) {
            if (list[i].id == quiz.id) {
                for (j in 0 until list[i].choices.size) {
                    if (list[i].choices[j] == answer)
                        list[i].studentSelection = j
                }
                break
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_quiz_mcq, container, false)
        recycleView = view.quiz_mcq_list
        getBaseActivity()?.setToolbarGone()
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            quizSetInfo = bundle.getSerializable("quiz set info") as SetData
        }
        setTitle(quizSetInfo.name!!)

        loadQuizList()

        view.quizSubmitBtn.setOnClickListener {
            if (checkAllAnswered()) {
                if (stopCountDown) {
                    SharedPref.back = false
                    switchFragmentWithNonBackStack(SubmitQuiz().newInstance(list, quizSetInfo.id!!))
                }
                else stopCountDown = true
            }
            else Toast.makeText(context, "Please answer all question before you submit", Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun setRecycleView(quizList: ArrayList<QuizData>) {
        try {
            recycleAdapter = QuizMcqAdapter(context!!, quizList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.setItemViewCacheSize(quizList.size)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("Quiz MCQ Adapter error:", e.toString())
        }
    }

    private fun loadQuizList() {
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

                            countDown((list.size * 90).toLong())
//                            countDown((20).toLong())
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["set_id"] = quizSetInfo.id.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val answerList = ArrayList<String>()
        val tempList = ArrayList<String>()

        for (i in 0 until obj.length()) {
            list.add(QuizData(
                    obj.getJSONObject(i).getInt("qz_id"),
                    obj.getJSONObject(i).getString("qz_qstn"),
                    obj.getJSONObject(i).getString("qz_ans"),
                    obj.getJSONObject(i).getString("qz_ans")))

            answerList.add(obj.getJSONObject(i).getString("qz_ans"))
        }

        for (i in 0 until list.size) {
            if (list[i].choices.size < 3) {
                tempList.clear()
                tempList.addAll(answerList)
                tempList.filterString(list[i].answer!!)
                tempList.shuffleString()

                Log.d("answer", list[i].answer)
                for (j in 0 until tempList.size) {
//                    Log.d("tempList got", tempList[j])
                }

//                Log.d("templist de size", tempList.size.toString())

                list[i].choices.add(tempList[0])
                list[i].choices.add(tempList[1])
                list[i].choices.add(tempList[2])

//                Log.d("templist de size", tempList.size.toString())

                list[i].choices.shuffleString()
            }
        }

        setRecycleView(list)
    }

    private fun checkAllAnswered() : Boolean {
        for (i in 0 until list.size) {
            if (list[i].studentSelection < 0 || list[i].studentSelection > 4)
                return false
        }
        return true
    }

    private fun countDown(getTime : Long) {
        val startTime = getTime * 1000
        SharedPref.back = true

        object : CountDownTimer(startTime, 1000) {

            override fun onTick(millisUntilFinished : Long) {
                if (!stopCountDown) {
                    val timeLeft = "seconds remaining: " + millisUntilFinished / 1000 + "Seconds Left"
                    view!!.quiz_timer.text = timeLeft
                }
                else {
                    cancel()
                    SharedPref.back = false
                    switchFragmentWithNonBackStack(SubmitQuiz().newInstance(list, quizSetInfo.id!!))
                }
            }

            override fun onFinish() {
                view!!.quiz_timer.text = getString(R.string.TimerEnd)
                Toast.makeText(context, "Wait me implement", Toast.LENGTH_LONG).show()
            }
        }.start()
    }
}