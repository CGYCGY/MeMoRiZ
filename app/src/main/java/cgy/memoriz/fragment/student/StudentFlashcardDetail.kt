package cgy.memoriz.fragment.student

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
import cgy.memoriz.data.FlashcardData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.DialogFactory
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_quiz_detail.view.*
import org.json.JSONException
import org.json.JSONObject

class StudentFlashcardDetail : MainActivityBaseFragment() {

    private var dialogFactory = DialogFactory()

    fun newInstance(flashcard : FlashcardData, setID: Int): StudentFlashcardDetail{
        val args = Bundle()
        args.putSerializable("flashcard detail", flashcard)
        args.putInt("flashcard id", setID)
        val fragment = StudentFlashcardDetail()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_detail, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val flashcard : FlashcardData = bundle.getSerializable("flashcard detail") as FlashcardData
            val setID= bundle.getInt("flashcard id")

            view.edit_quiz_question.text = Editable.Factory.getInstance().newEditable(flashcard.card1)
            view.edit_quiz_answer.text = Editable.Factory.getInstance().newEditable(flashcard.card1)
            view.updateQuizBtn.text = getString(R.string.updateFC)
            view.deleteQuizBtn.text = getString(R.string.deleteFC)

            setTitle("Flashcard Editor")

            view.updateQuizBtn.setOnClickListener {
                view.hideKeyboard()
                update(flashcard)
            }

            view.deleteQuizBtn.setOnClickListener {
                dialogFactory.createTwoButtonDialog(context!!, "ALERT!", "Do you want to delete this flashcard?",
                        DialogInterface.OnClickListener { dialog, which -> delete(flashcard, setID) }).show()
            }

        }else {
            Log.e("missing FC Data", "StudentFlashcardDetail got error!")
        }

        return view
    }

    private fun update(flashcard: FlashcardData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateFlashcard,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Flashcard updated successfully") {
                            super.getBaseActivity()!!.onBackPressed()
//                            val intent = Intent(context, MainMenuActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            startActivity(intent)
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

                params["fc_id"] = flashcard.id.toString()
                params["fc_selection"] = "1010"
                params["card1"] = view?.edit_quiz_question?.text.toString()
                params["card2"] = view?.edit_quiz_answer?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun delete(flashcard: FlashcardData, setID: Int) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlDeleteFlashcard,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Flashcard removed successfully") {
                            super.getBaseActivity()!!.onBackPressed()
//                            val intent = Intent(context, MainMenuActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            startActivity(intent)
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

                params["fc_id"] = flashcard.id.toString()
                params["fcs_id"] = setID.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}