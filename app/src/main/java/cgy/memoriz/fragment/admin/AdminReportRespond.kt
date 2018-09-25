package cgy.memoriz.fragment.admin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.data.ReportData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.base_list_report.view.*
import kotlinx.android.synthetic.main.fragment_admin_report.view.*
import org.json.JSONException
import org.json.JSONObject



class AdminReportRespond : MainActivityBaseFragment() {
    private var typeSelected : String ?= null

    val type = arrayListOf("ON-HOLD", "SOLVING", "SOLVED", "CLOSED")

    fun newInstance(report: ReportData): AdminReportRespond{
        val args = Bundle()
        args.putSerializable("report detail", report)
        val fragment = AdminReportRespond()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_report, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val report : ReportData = bundle.getSerializable("report detail") as ReportData

            typeSelected = report.status

            for (x in 1 until type.size) {
                if (type[x] == report.status) {
                    type[0] = type[x].also { type[x] = type[0] }
                }
            }

            view.report_title.text = report.title
            view.report_type.text = report.type
            view.report_body.text = report.body
            view.report_datetime.text = report.datetime
            view.report_stat.text = report.status

            view.report_change_status.setItems(type)
            view.report_change_status.setOnItemSelectedListener { view, position, id, item ->
                Snackbar.make(view, "$item is selected", Snackbar.LENGTH_LONG).show()
                typeSelected = type[position]
            }

            view.report_change_status.setOnNothingSelectedListener {
                typeSelected = report.status
            }

            view.changeRSBtn.setOnClickListener {
                update(report)
            }

            setTitle("Question Editor")

        }else {
            Log.e("missing ReportData", "AdminReportRespond got error!")
        }

        return view
    }

    private fun update(report: ReportData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateReport,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Report updated successfully") {
                            super.getBaseActivity()!!.onBackPressed()
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

                params["rpt_id"] = report.id.toString()
                params["rpt_cont"] = typeSelected.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}