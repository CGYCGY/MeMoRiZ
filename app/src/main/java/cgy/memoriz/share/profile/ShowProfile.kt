package cgy.memoriz.share.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.PermissionChecker
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.data.UserData
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.SDialog
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile_show.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ShowProfile : MainActivityBaseFragment() {
    private lateinit var spDialog : SDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_show, container, false)

        spDialog = SDialog("Loading...", context!!)

        setTitle("Profile")
        loadUserProfile()

        view.changeProfilePictureBtn.setOnClickListener {
            switchFragment(EditProfilePicture())
        }
        view.changeProfileDetailBtn.setOnClickListener {

        }

        return view
    }

    private fun loadUserProfile() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlGetUserProfile,
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
                            val userProfile = jsonToArrayList(obj.getJSONArray("table"))
                            setupUserProfile(userProfile)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["u_email"] = SharedPref.userEmail
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun jsonToArrayList(obj : JSONArray) : UserData {
        val userProfile = UserData(SharedPref.userEmail, SharedPref.userName)

        for (i in 0 until obj.length()) {
            userProfile.address = obj.getJSONObject(i).getString("u_place")
            userProfile.experience = obj.getJSONObject(i).getString("u_exp")
            userProfile.picture = obj.getJSONObject(i).getString("u_pic")
        }
        return userProfile
    }

    private fun downloadProfilePic(url : String, imageName : String) {
        if (PermissionChecker.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                PermissionChecker.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        spDialog.showDialog()
        Picasso.get()
                .load(url)
                .into(view!!.profile_photo_show)
        spDialog.hideDialog()

        //save to phone storage
//        Picasso.get()
//                .load(url)
//                .into(JExtension.getTarget(imageName))
    }

    @SuppressLint("SetTextI18n")
    private fun setupUserProfile(userProfile: UserData) {
        view!!.profile_name_show.text = "Name: " + SharedPref.userName
        view!!.profile_email_show.text = "Email: " + SharedPref.userEmail
        view!!.profile_exp_show.text = "Education Level: " + userProfile.experience
        view!!.profile_place_show.text = "Address: " + userProfile.address

        val location = URLEndpoint.urlLoadImage + userProfile.picture!!.substring(9, userProfile.picture!!.length)
        val imageName = "MeMoRiZ/Profile/" + userProfile.picture!!.substring(9, userProfile.picture!!.length)
        downloadProfilePic(location, imageName)
    }
}