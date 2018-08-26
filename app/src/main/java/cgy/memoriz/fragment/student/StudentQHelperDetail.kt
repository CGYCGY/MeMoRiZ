package cgy.memoriz.fragment.student

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
import cgy.memoriz.data.QuestionData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_qhelper_detail.view.*
import org.json.JSONException
import org.json.JSONObject

class StudentQHelperDetail : MainActivityBaseFragment() {

    fun newInstance(question: QuestionData): StudentQHelperDetail{
        val args = Bundle()
        args.putSerializable("question detail", question)
        val fragment = StudentQHelperDetail()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_qhelper_detail, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val question : QuestionData = bundle.getSerializable("question detail") as QuestionData

            view.qhelper_detail_title.text = Editable.Factory.getInstance().newEditable(question.title)
            view.qhelper_detail_body.text = Editable.Factory.getInstance().newEditable(question.body)
            view.qhelper_detail_datetime.text = Editable.Factory.getInstance().newEditable(question.datetime)
            view.qhelper_detail_condition.text = Editable.Factory.getInstance().newEditable(question.condition)

            setTitle("Question Editor")

            view.updateQuestionBtn.setOnClickListener {
                update(question)
            }

        }else {
            Log.e("missing QuestionData", "StudentQHelperDetail got error!")
        }

        return view
    }

    private fun update(question: QuestionData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateQuestion,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Question updated successfully") {
                            super.getBaseActivity()!!.onBackPressed()
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

                params["qstn_id"] = question.id.toString()
                params["qstn_title"] = view?.qhelper_detail_title?.text.toString()
                params["qstn_body"] = view?.qhelper_detail_body?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}