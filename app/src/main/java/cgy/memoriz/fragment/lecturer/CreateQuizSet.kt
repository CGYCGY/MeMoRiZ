package cgy.memoriz.fragment.lecturer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_create_set.view.*
import org.json.JSONException
import org.json.JSONObject

class CreateQuizSet : MainActivityBaseFragment() {

    private var textGet : String ?= null

    fun newInstance(text : String) : CreateQuizSet {
        val args = Bundle()
        args.putString("text", text)
        val fragment = CreateQuizSet()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_set, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getString("text")
            setTitle("$textGet")
        }else {
            setTitle("Create Quiz Set")
        }

        view.set_name.hint = "Quiz Set Name"

        view.createSetBtn.setOnClickListener {
            if (view.set_name.text.isNotBlank()) {
                create()
                view.hideKeyboard()
            }
        }

        return view
    }

    private fun create() {
//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertSet,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Set created successfully") getBaseActivity()!!.onBackPressed()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            //          pack the registration info to POST it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["u_email"] = SharedPref.userEmail
                params["set_name"] = view?.set_name?.text.toString()
                params["set_type"] = "Quiz"

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
