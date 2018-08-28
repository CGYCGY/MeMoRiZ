package cgy.memoriz.fragment.lecturer

import android.os.Bundle
import android.text.Editable
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
import kotlinx.android.synthetic.main.fragment_quiz_detail.view.*
import org.json.JSONException
import org.json.JSONObject

class LecturerQuizDetail : MainActivityBaseFragment() {

    fun newInstance(quiz: QuizData): LecturerQuizDetail{
        val args = Bundle()
        args.putSerializable("quiz detail", quiz)
        val fragment = LecturerQuizDetail()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_detail, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val quiz : QuizData = bundle.getSerializable("quiz detail") as QuizData

            view.edit_quiz_question.text = Editable.Factory.getInstance().newEditable(quiz.question)
            view.edit_quiz_answer.text = Editable.Factory.getInstance().newEditable(quiz.answer)

            setTitle("Quiz Editor")

            view.updateQuizBtn.setOnClickListener {
                update(quiz)
            }

        }else {
            Log.e("missing QuizData", "LecturerQuizDetail got error!")
        }

        return view
    }

    private fun update(quiz: QuizData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateQuestion,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Quiz updated successfully") {
                            super.getBaseActivity()!!.onBackPressed()
//                            val intent = Intent(context, MainMenuActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            startActivity(intent)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            //          pack the registration info to POSt it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["qz_id"] = quiz.id.toString()
                params["qz_qstn"] = view?.edit_quiz_question?.text.toString()
                params["qz_ans"] = view?.edit_quiz_answer?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}