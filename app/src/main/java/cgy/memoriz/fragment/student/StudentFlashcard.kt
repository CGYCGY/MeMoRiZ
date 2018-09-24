package cgy.memoriz.fragment.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.data.FCSetData
import cgy.memoriz.data.FlashcardData
import cgy.memoriz.fragment.MainActivityBaseFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_student_fc_back.view.*
import kotlinx.android.synthetic.main.fragment_student_fc_front.view.*
import kotlinx.android.synthetic.main.fragment_student_flashcard.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StudentFlashcard : MainActivityBaseFragment() {
    private var fcSet = FCSetData()
    private var currentPosition = 0

    fun newInstance(fcSetInfo: FCSetData): StudentFlashcard{
        val args = Bundle()
        args.putSerializable("flashcard set detail", fcSetInfo)
        val fragment = StudentFlashcard()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_flashcard, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val fcSet : FCSetData = bundle.getSerializable("flashcard set detail") as FCSetData
//            this.fcSet = fcSet

            loadFlashcardList(fcSet)

            setTitle(fcSet.name.toString())
        }else {
            Log.e("error", "missing flashcard set")
            setTitle("Flashcard")
        }

        view.fc_image1.visibility = GONE
        view.fc_image2.visibility = GONE
        view.previousFCBtn.visibility = INVISIBLE
        view.fc_back.visibility = GONE

        view.fc_front.setOnClickListener {
            if (view.fc_back.visibility == GONE)
                view.fc_back.visibility = VISIBLE

            view.flashcard.flipTheView()
        }

        view.fc_back.setOnClickListener {
            view.flashcard.flipTheView()
        }

        view.previousFCBtn.setOnClickListener {
            currentPosition -= 1
            Log.d("current position", currentPosition.toString())
            startFlashcard()
        }

        view.nextFCBtn.setOnClickListener {
            currentPosition += 1
            Log.d("current position", currentPosition.toString())
            startFlashcard()

        }

//        view.flashcard.setOnFlipListener { easyFlipView, newCurrentSide ->
//            Toast.makeText(context, "Flip Completed! New Side is: " + newCurrentSide, Toast.LENGTH_LONG).show()
//        }

        return view
    }

    private fun startFlashcard() {

        if (currentPosition == 0) {
            view!!.previousFCBtn.visibility = INVISIBLE
            view!!.nextFCBtn.visibility = VISIBLE
        }
        else if (currentPosition == fcSet.fcList.size-1) {
            view!!.previousFCBtn.visibility = VISIBLE
            view!!.nextFCBtn.visibility = INVISIBLE
        }
        else {
            view!!.previousFCBtn.visibility = VISIBLE
            view!!.nextFCBtn.visibility = VISIBLE
        }

        setupFlashcard(fcSet.fcList[currentPosition])
    }

    private fun setupFlashcard(flashcard : FlashcardData) {

        if (flashcard.selection == "1010") {
            view!!.fc_image1.visibility = GONE
            view!!.fc_image2.visibility = GONE

            view!!.fc_card1.text = flashcard.card1
            view!!.fc_card2.text = flashcard.card2
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

        fcSet.fcList = list
//        fcSet.fcList.shuffleFlashcard()
        startFlashcard()
    }
}