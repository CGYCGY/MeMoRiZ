package cgy.memoriz.fragment.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.data.QuizData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject



class SubmitQuiz : MainActivityBaseFragment() {
    private lateinit var quizList : ArrayList<QuizData>
    private var quizResultID = 0
    private var setID = 0

    fun newInstance(text : String) : SubmitQuiz {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = SubmitQuiz()
        fragment.arguments = args
        return fragment
    }

    fun newInstance(quiz : ArrayList<QuizData>, setID : Int): SubmitQuiz{
        val args = Bundle()
        args.putSerializable("quiz list", quiz)
        args.putInt("set id", setID)
        val fragment = SubmitQuiz()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_submit_quiz, container, false)

        setTitle("Wait a moment...")
        val bundle = arguments
        if (bundle != null) {
            @Suppress("UNCHECKED_CAST")
            quizList = bundle.getSerializable("quiz list") as ArrayList<QuizData>
            setID = bundle.getInt("set id")

            saveQuizResult(setID)
        }
        return view
    }

    private fun saveQuizResult(setID : Int){
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertQuizResult,
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
                            quizResultID = jsonToArrayList(obj.getJSONArray("table"))

                            if (quizResultID != 0) {
                                for (i in 0 until quizList.size) {
                                    saveQuizResultRecord(quizResultID, quizList[i].id!!, checkAnswerNumber(quizList[i]))
                                }
                            }
                            else Log.e("sql query error", "quizResultID fail to get")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["set_id"] = setID.toString()
                params["qr_mark"] = getMark().toString()

                Log.d("in getParam liao", "sdadasdasdadadasd")
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
        Log.d("me outside liao", "sdasdasdadadadadasdds")
    }

    private fun saveQuizResultRecord(quizResultID : Int, quizID : Int, choice : Int) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertQuizResultRecord,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)

                        Log.d("quiz result record", obj.getString("message"))

//                        switchFragment(StudentQuizResult().newInstance(quizList, setID, quizResultID))
                        switchFragmentWithNonBackStack(StudentQuizResult().newInstance(quizList))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["qr_id"] = quizResultID.toString()
                params["qz_id"] = quizID.toString()
                params["qrr_choice"] = choice.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
  private fun jsonToArrayList(obj : JSONArray) : Int {
        return obj.getJSONObject(0).getInt("qr_id")
    }

    private fun getMark() : Int {
        var mark = 0

        for (i in 0 until quizList.size) {
            if (checkAnswer(quizList[i]))
                mark++
        }
        return mark
    }

    private fun checkAnswer(quiz : QuizData) : Boolean {
        return quiz.answer == quiz.choices[quiz.studentSelection]
    }

    private fun checkAnswerNumber(quiz : QuizData) : Int {
        if (quiz.answer == quiz.choices[quiz.studentSelection])
            return 1
        else return 0
    }
}