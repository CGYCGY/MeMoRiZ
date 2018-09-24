package cgy.memoriz.fragment.lecturer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.data.ClassData
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
    private lateinit var classInfo : ClassData

    fun newInstance(classInfo: ClassData): CreateQuizSet {
        val args = Bundle()
        args.putSerializable("class info", classInfo)
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
            classInfo = bundle.getSerializable("class info") as ClassData
        }else {
            Log.e("error", "missing bundle data")
        }

        setTitle("Create Quiz Set")

        view.set_name.hint = "Quiz Set Name"

        view.createSetBtn.setOnClickListener {
            if (view.set_name.text.isNotBlank()) {
                create(classInfo.id!!)
                view.hideKeyboard()
            }
        }

        return view
    }

    private fun create(classID : Int) {
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

                params["cls_id"] = classID.toString()
                params["set_name"] = view?.set_name?.text.toString()
                params["set_type"] = "Quiz"

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
