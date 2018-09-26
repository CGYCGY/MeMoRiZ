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
import cgy.memoriz.adapter.QuizSetAdapterInterface
import cgy.memoriz.adapter.SlideSetAdapter
import cgy.memoriz.data.ClassData
import cgy.memoriz.data.SetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.DialogFactory
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_lecturer_slideset.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LecturerSlideSet : MainActivityBaseFragment(),QuizSetAdapterInterface {
    private lateinit var recycleAdapter: SlideSetAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var classInfo : ClassData

    private var dialogFactory = DialogFactory()

    fun newInstance(text : String) : LecturerSlideSet {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = LecturerSlideSet()
        fragment.arguments = args
        return fragment
    }

    fun newInstance(classInfo : ClassData): LecturerSlideSet{
        val args = Bundle()
        args.putSerializable("class info", classInfo)
        val fragment = LecturerSlideSet()
        fragment.arguments = args
        return fragment
    }

    //reuse quiz adapter interface
    override fun onClick(quiz: SetData) {
        Log.d("CLICKED HERE YOUR DATA", quiz.name)
        switchFragment(LecturerSlide().newInstance(quiz))
    }

    override fun onLongClick(quiz: SetData) {
        Log.d("LONG CLICKED! YOUR DATA", quiz.name)
        dialogFactory.createOneButtonDialog(context!!, "Slide Set Name", quiz.name.toString(),
                "back").show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lecturer_slideset, container, false)
        recycleView = view.lecturer_slide_set
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            classInfo = bundle.getSerializable("class info") as ClassData
            setTitle("Slide Set List")
        }else {
            setTitle("Slide Set List")
        }

        loadSlideList()

        view.createSlideSetBtn.setOnClickListener {
            switchFragment(CreateSlideSet().newInstance(classInfo))
        }

        return view
    }

    private fun setRecycleView(slideList: ArrayList<SetData>) {
        try {
            recycleAdapter = SlideSetAdapter(context!!, slideList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("SlideSet Adapter error", e.toString())
        }
    }

    private fun loadSlideList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetSet,
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

                params["cls_id"] = classInfo.id.toString()
                params["set_type"] = "Slide"

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<SetData>()

        for (i in 0 until obj.length())
            list.add(SetData(
                    obj.getJSONObject(i).getInt("set_id"),
                    obj.getJSONObject(i).getString("set_name"),
                    obj.getJSONObject(i).getInt("set_size")))

        setRecycleView(list)
    }
}