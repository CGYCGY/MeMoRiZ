package cgy.memoriz.fragment.student

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
import cgy.memoriz.adapter.FlashcardAdapter
import cgy.memoriz.adapter.FlashcardAdapterInterface
import cgy.memoriz.data.FCSetData
import cgy.memoriz.data.FlashcardData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.base_list_flashcardset.view.*
import kotlinx.android.synthetic.main.fragment_fcset_detail.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StudentFCSetDetail : MainActivityBaseFragment(), FlashcardAdapterInterface {
    private lateinit var recycleAdapter: FlashcardAdapter
    private lateinit var recycleView: RecyclerView

    private var fcSetInfo = FCSetData()

    fun newInstance(fcSetInfo : FCSetData): StudentFCSetDetail{
        val args = Bundle()
        args.putSerializable("flashcard set detail", fcSetInfo)
        val fragment = StudentFCSetDetail()
        fragment.arguments = args
        return fragment
    }

    override fun onClick(flashcard : FlashcardData) {
        Log.d("CLICKED HERE YOUR DATA", flashcard.card1)
        switchFragment(StudentFlashcardDetail().newInstance(flashcard, fcSetInfo.id!!))
    }

    override fun onLongClick(flashcard : FlashcardData) {
        Log.d("LONG CLICKED! YOUR DATA", flashcard.card1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fcset_detail, container, false)
        recycleView = view.fcsSetDetail
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val fcSetInfo : FCSetData = bundle.getSerializable("flashcard set detail") as FCSetData
            val fcCount = "Flashcard Count: " + fcSetInfo.size.toString()
            this.fcSetInfo = fcSetInfo

            view.fcset_name.text = fcSetInfo.name
            view.fcset_size.text = fcCount

            setTitle("Flashcard List")

            loadFlashcardList(fcSetInfo)

            view.addFlashcardBtn.setOnClickListener{
//                switchFragment(CreateFlashcardMode().newInstance(fcSetInfo.id.toString()))
                switchFragment(CreateFlashcard().newInstance(fcSetInfo.id.toString(), "1010"))
            }
            view.viewFlashcardBtn.setOnClickListener{
                switchFragment(StudentFlashcard().newInstance(fcSetInfo))
            }
        }else {
            Log.e("missing FCSet Data", "StudentFCSetDetail got error!")
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val fcCount = "Flashcard Count: " + fcSetInfo.size.toString()
        view!!.fcset_size.text = fcCount
        loadFlashcardList(fcSetInfo)
    }

    private fun setRecycleView(flashcardList: ArrayList<FlashcardData>) {
        try {
            recycleAdapter = FlashcardAdapter(context!!, flashcardList, this)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("Flashcard Adapter error", e.toString())
        }
    }

    private fun loadFlashcardList(fcSetInfo: FCSetData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetFlashcard,
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

                Log.d("flashcard id", fcSetInfo.id.toString())
                params["fcs_id"] = fcSetInfo.id.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) {
        val list = ArrayList<FlashcardData>()

        for (i in 0 until obj.length()) {
            val selection = obj.getJSONObject(i).getString("fc_selection")
            var card1 = ""
            var card2 = ""

            if (selection == "1010") {
                card1 = obj.getJSONObject(i).getString("fc_card1")
                card2 = obj.getJSONObject(i).getString("fc_card2")
            }
            else if (selection == "0101") {
                card1 = obj.getJSONObject(i).getString("fc_img1")
                card2 = obj.getJSONObject(i).getString("fc_img2")
            }
            else if (selection == "0110") {
                card1 = obj.getJSONObject(i).getString("fc_img1")
                card2 = obj.getJSONObject(i).getString("fc_card2")
            }
            else if (selection == "1001") {
                card1 = obj.getJSONObject(i).getString("fc_card1")
                card2 = obj.getJSONObject(i).getString("fc_img2")
            }
            else Log.e("FCSet selection", "wrong data format")

            list.add(FlashcardData(
                    obj.getJSONObject(i).getInt("fc_id"),
                    selection,
                    card1,
                    card2))
        }

        setRecycleView(list)
    }
}