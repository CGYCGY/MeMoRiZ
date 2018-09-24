package cgy.memoriz.fragment.lecturer

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.adapter.SlideAdapter
import cgy.memoriz.adapter.SlideAdapterInterface
import cgy.memoriz.data.SetData
import cgy.memoriz.data.SlideData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.base_list_slide_set.view.*
import kotlinx.android.synthetic.main.fragment_lecturer_slide.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LecturerSlide : MainActivityBaseFragment(), SlideAdapterInterface {
    private lateinit var recycleAdapter: SlideAdapter
    private lateinit var recycleView: RecyclerView

    private var set = SetData()

    fun newInstance(set: SetData): LecturerSlide{
        val args = Bundle()
        args.putSerializable("set", set)
        val fragment = LecturerSlide()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(slide : SlideData) {
        Log.d("CLICKED HERE YOUR DATA", slide.id.toString())
        switchFragment(LecturerSlideDetail().newInstance(slide, this.set.id!!))
    }

    override fun onLongClick(slide : SlideData) {
        Log.d("LONG CLICKED! YOUR DATA", slide.id.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lecturer_slide, container, false)
        recycleView = view.slide_list
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            set  = bundle.getSerializable("set") as SetData

            val slideSetSize = "Slide Set Size: " + set.size.toString()
            view.slide_set_name.text = set.name
            view.slide_set_size.text = slideSetSize

            setTitle("Slide List")

            loadSlideList(set)

            view.addSlideBtn.setOnClickListener{
                switchFragment(AddSlide().newInstance(set))
            }
        }else {
            Log.e("missing SlideData", "LecturerSlide got error!")
        }

        return view
    }

    override fun onResume() {
        loadSlideList(set)
        super.onResume()
    }

    private fun setRecycleView(slideList: ArrayList<SlideData>) {
        try {
            recycleAdapter = SlideAdapter(context!!, slideList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("Slide Adapter error:", e.toString())
        }
    }

    private fun loadSlideList(set: SetData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetSlide,
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

                params["set_id"] = set.id.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<SlideData>()

        for (i in 0 until obj.length())
            list.add(SlideData(
                    obj.getJSONObject(i).getInt("sl_id"),
                    obj.getJSONObject(i).getString("sl_cont")))

        set.size = list.size

        val slideSetSize = "Slide Set Size: " + set.size.toString()
        view!!.slide_set_name.text = set.name
        view!!.slide_set_size.text = slideSetSize
        setRecycleView(list)
    }
}