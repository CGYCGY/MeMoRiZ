package cgy.memoriz.fragment.report

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.adapter.ReportAdapter
import cgy.memoriz.adapter.ReportAdapterInterface
import cgy.memoriz.data.ReportData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_view_report.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ViewReport : MainActivityBaseFragment(),ReportAdapterInterface {
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var recycleView: RecyclerView

    private var textGet : String ?= null

    fun newInstance(text : String) : ViewReport {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = ViewReport()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(report: ReportData) {
        Log.d("CLICKED HERE YOUR DATA", report.title)
//        switchFragment(StudentQSolverDetail().newInstance(question))
    }

    override fun onLongClick(report: ReportData) {
        Log.d("LONG CLICKED! YOUR DATA", report.title)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_report, container, false)
        recycleView = view.report
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getString("value1")
            setTitle("$textGet")
        }else {
            setTitle("View Report")
        }

        loadReportList()

        return view
    }

    private fun setRecycleView(reportList: ArrayList<ReportData>) {
        try {
            reportAdapter = ReportAdapter(context!!, reportList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = reportAdapter
        } catch (e: NullPointerException) {
            Log.d("Report Adapter error:", e.toString())
        }
    }

    private fun loadReportList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetReport,
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
                            jsonToArrayList(obj.getJSONArray("table"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["ad_email"] = SharedPref.userEmail
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<ReportData>()

        for (i in 0 until obj.length())
            list.add(ReportData(
                    obj.getJSONObject(i).getInt("rpt_id"),
                    obj.getJSONObject(i).getString("rpt_title"),
                    obj.getJSONObject(i).getString("rpt_type"),
                    obj.getJSONObject(i).getString("rpt_body"),
                    obj.getJSONObject(i).getString("rpt_datetime"),
                    obj.getJSONObject(i).getString("rpt_stat")))

        setRecycleView(list)
    }
}