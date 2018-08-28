package cgy.memoriz.fragment.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import kotlinx.android.synthetic.main.fragment_create_report.view.*
import org.json.JSONException
import org.json.JSONObject

class CreateReport : MainActivityBaseFragment(),AdapterView.OnItemSelectedListener {

    private var textGet : String ?= null
    private var typeSelected : String ?= null
    private val type = arrayOf("Flashcard Section", "Question Section", "Class Section", "Students Hall Section", "Others")

    fun newInstance(text : String) : CreateReport {
        val args = Bundle()
        args.putString("text", text)
        val fragment = CreateReport()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_report, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getString("text")
            setTitle("$textGet")
        }else {
            setTitle("Create Report")
        }

        view.create_report_type!!.onItemSelectedListener = this
        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, type)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        view.create_report_type!!.adapter = arrayAdapter

        view.createReportBtn.setOnClickListener {
            if (view.create_report_title.text.isNotBlank() && !typeSelected.isNullOrBlank() && view.create_report_body.text.isNotBlank()) {
                create()
                view.hideKeyboard()
            }
        }

        return view
    }

    private fun create() {
//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertReport,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Report added successfully") {
                            getBaseActivity()!!.onBackPressed()
//                            switchFragment(StudentMainMenu())
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

                params["u_email"] = SharedPref.userEmail
                params["sl"] = "Student"
                params["rpt_title"] = view?.create_report_title?.text.toString()
                params["rpt_type"] = typeSelected.toString()
                params["rpt_body"] = view?.create_report_body?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
        typeSelected = type[position]
    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {

    }
}
