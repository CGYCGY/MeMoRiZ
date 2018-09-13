package cgy.memoriz.fragment.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_create_flashcard.view.*
import org.json.JSONException
import org.json.JSONObject

class CreateFlashcard : MainActivityBaseFragment() {

//    private lateinit var uploadingDialog : SDialog
//    private lateinit var encodedImage : String
//    private lateinit var userChosenTask : String
//
//    private val requestCamera = 0
//    private val selectFile = 1
//    var thumbnail1 : Bitmap? = null
//    var thumbnail2 : Bitmap? = null

    fun newInstance(id : String, mode : String) : CreateFlashcard {
        val args = Bundle()
        args.putString("id", id)
        args.putString("mode", mode)
        val fragment = CreateFlashcard()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_flashcard, container, false)
        /*
         * Get the data from previous fragment
         */
        var fcsID = ""
        var fcdMode = ""
        val bundle = arguments
        if (bundle != null) {
            fcsID = bundle.getString("id")
            fcdMode = bundle.getString("mode")

            setTitle("Add New Flashcard")
        }else {
            Log.e("error", "bundle data missing")
        }

        modeSwitcher(fcdMode)

//        view.create_flashcard_image1.setOnTouchListener { _, _ ->
//            selectImage(1)
//            false
//        }

        view.createFlashcardBtn.setOnClickListener {
            if (fcdMode == "1010") {
                if (view.create_flashcard_card1.text.isNotBlank() && view.create_flashcard_card2.text.isNotBlank())
                    create(fcsID)
            }
//            else if (fcdMode == "0101") {
//                if (view.create_flashcard_image1.text.isNotBlank() && view.create_flashcard_image2.text.isNotBlank())
//                    create(fcsID)
//            }
//            else if (fcdMode == "0110") {
//                if (view.create_flashcard_image1.text.isNotBlank() && view.create_flashcard_card2.text.isNotBlank())
//                    create(fcsID)
//            }
//            else if (fcdMode == "1001") {
//                if (view.create_flashcard_card1.text.isNotBlank() && view.create_flashcard_image2.text.isNotBlank())
//                    create(fcsID)
//            }

            view.hideKeyboard()
        }

        return view
    }

    private fun modeSwitcher(fcdMode : String) {
        if (fcdMode == "1010") {
            view!!.create_flashcard_image1.visibility = GONE
            view!!.create_flashcard_image2.visibility = GONE
        }
        else if (fcdMode == "0101") {
            view!!.create_flashcard_card1.visibility = GONE
            view!!.create_flashcard_card2.visibility = GONE
        }
        else if (fcdMode == "0110") {
            view!!.create_flashcard_card1.visibility = GONE
            view!!.create_flashcard_image2.visibility = GONE
        }
        else if (fcdMode == "1001") {
            view!!.create_flashcard_image1.visibility = GONE
            view!!.create_flashcard_card2.visibility = GONE
        }
    }

//    private fun selectImage(num : Int) {
//        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
//
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Add Photo!")
//        builder.setItems(items) { dialog, item ->
//            val result = Utility.checkPermission(context!!)
//
//            if (items[item] == "Take Photo") {
//                userChosenTask = "Take Photo"
//
//                if (PermissionChecker.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                    requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCamera)
//                else
//                    cameraIntent()
//            }
//            else if (items[item] == "Choose from Library") {
//                userChosenTask = "Choose from Library"
//                if (result)
//                    galleryIntent()
//
//            }
//            else if (items[item] == "Cancel") {
//                dialog.dismiss()
//            }
//        }
//        builder.show()
//    }

    private fun create(fcsID : String) {
//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlInsertFlashcard,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Flashcard added successfully") {
                            getBaseActivity()!!.onBackPressed()
//                            switchFragment(StudentMainMenu())
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

                params["fcs_id"] = fcsID
                params["fc_selection"] = "1010"
                params["card1"] = view?.create_flashcard_card1?.text.toString()
                params["card2"] = view?.create_flashcard_card2?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
