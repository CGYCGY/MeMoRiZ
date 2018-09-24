package cgy.memoriz.fragment.lecturer

import android.content.DialogInterface
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
import cgy.memoriz.data.SlideData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.DialogFactory
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_slide_detail.view.*
import org.json.JSONException
import org.json.JSONObject

class LecturerSlideDetail : MainActivityBaseFragment() {

    private var dialogFactory = DialogFactory()

    fun newInstance(slide: SlideData, setID: Int): LecturerSlideDetail{
        val args = Bundle()
        args.putSerializable("slide detail", slide)
        args.putInt("set id", setID)
        val fragment = LecturerSlideDetail()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_slide_detail, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val slide : SlideData = bundle.getSerializable("slide detail") as SlideData
            val setID= bundle.getInt("set id")

            view.edit_slide_content.text = Editable.Factory.getInstance().newEditable(slide.content)

            setTitle("Slide Editor")

            view.updateSlideBtn.setOnClickListener {
                view.hideKeyboard()
                update(slide)
            }

            view.deleteSlideBtn.setOnClickListener {
                dialogFactory.createTwoButtonDialog(context!!, "ALERT!", "Do you want to delete this slide?",
                        DialogInterface.OnClickListener { dialog, which -> delete(slide, setID) }).show()
            }

        }else {
            Log.e("missing Slideata", "LecturerSlideDetail got error!")
        }

        return view
    }

    private fun update(slide: SlideData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateSlide,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Slide updated successfully") {
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

                params["sl_id"] = slide.id.toString()
                params["sl_cont"] = view?.edit_slide_content?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun delete(slide: SlideData, setID: Int) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlDeleteSlide,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Slide removed successfully") {
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

                params["sl_id"] = slide.id.toString()
                params["set_id"] = setID.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}