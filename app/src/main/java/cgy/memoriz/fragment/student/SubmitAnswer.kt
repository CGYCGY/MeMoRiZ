package cgy.memoriz.fragment.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.data.QuestionData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.base_list_qsolver.view.*
import kotlinx.android.synthetic.main.fragment_submit_answer.view.*
import org.json.JSONException
import org.json.JSONObject

class SubmitAnswer : MainActivityBaseFragment() {
    private var textGet : String ?= null

    fun newInstance(question: QuestionData): SubmitAnswer{
        val args = Bundle()
        args.putSerializable("question detail", question)
        val fragment = SubmitAnswer()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_submit_answer, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val question: QuestionData = bundle.getSerializable("question detail") as QuestionData

            view.qsolver_title.text = question.title
            view.qsolver_body.text = question.body
            view.qsolver_owner.text = question.owner
            view.qsolver_datetime.text = question.datetime
            view.qsolver_condition.text = question.condition

            setTitle("Question Answer")

            view.sa_submitBtn.setOnClickListener {
                if (view.sa_answer_body.text.isNotBlank()) {
                    insertAnswer(question)
                    view.hideKeyboard()
                }
            }
        } else {
            Log.e("missing QuestionData", "SubmitAnswer got error!")

        }

        return view
    }

    private fun insertAnswer(question: QuestionData) {
//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertAnswer,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Answer added successfully") {
                            super.getBaseActivity()?.onBackPressed()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            //          pack the registration info to POST it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["qstn_id"] = question.id.toString()
                params["u_email"] = SharedPref.userEmail
                params["ans_body"] = view?.sa_answer_body?.text.toString()
                params["qstn_cond"] = "Answer being verify"

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}