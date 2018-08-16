package cgy.memoriz.fragment.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.*
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.fragment.StudentMainMenu
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_create_question.view.*
import org.json.JSONException
import org.json.JSONObject

class CreateQuestion : MainActivityBaseFragment() {

    private var textGet : String ?= null

    fun newInstance(text : String) : CreateQuestion {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = CreateQuestion()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_question, container, false)
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

        view.cq_createBtn.setOnClickListener {
            if (view.cq_question_title.text.isNotBlank() && view.cq_question_body.text.isNotBlank()) {
                create()
                view.hideKeyboard()
            }
        }

        return view
    }

    private fun create() {
//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertQuestion,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Question added successfully") {
                            switchFragment(StudentMainMenu())
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

                params["ad_email"] = SharedPref.userEmail
                params["qstn_title"] = view?.cq_question_title?.text.toString()
                params["qstn_body"] = view?.cq_question_body?.text.toString()
                params["qstn_cond"] = "Waiting for answer"

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
