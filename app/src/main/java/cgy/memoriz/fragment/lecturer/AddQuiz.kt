package cgy.memoriz.fragment.lecturer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.data.SetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_create_quiz.view.*
import org.json.JSONException
import org.json.JSONObject

class AddQuiz : MainActivityBaseFragment() {

    private var textGet : String ?= null

    fun newInstance(quiz: SetData): AddQuiz{
        val args = Bundle()
        args.putSerializable("quiz", quiz)
        val fragment = AddQuiz()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_quiz, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        val quiz : SetData = bundle!!.getSerializable("quiz") as SetData

            setTitle("Add Quiz")

        view.createQuizBtn.setOnClickListener {
            if (view.create_quiz_question.text.isNotBlank() && view.create_quiz_answer.text.isNotBlank()) {
                create(quiz.id)
                view.hideKeyboard()
            }
        }

        return view
    }

    private fun create(setID : Int?) {
//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertQuiz,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Quiz created successfully") getBaseActivity()!!.onBackPressed()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            //          pack the registration info to POSt it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["set_id"] = setID.toString()
                params["qz_qstn"] = view?.create_quiz_question?.text.toString()
                params["qz_ans"] = view?.create_quiz_answer?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
