package cgy.memoriz.share.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import cgy.memoriz.others.JExtension
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
import java.io.File
import java.util.*


class ShowProfile : MainActivityBaseFragment() {
    private lateinit var spDialog : SDialog
    private lateinit var file : File
    private lateinit var user : UserData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_show, container, false)

        spDialog = SDialog("Loading...", context!!)
        file = File("not exist")

        setTitle("Profile")
        loadUserProfile()

        view.changeProfilePictureBtn.setOnClickListener {
            switchFragment(EditProfilePicture().newInstance(file))
        }

        view.changeProfileDetailBtn.setOnClickListener {
            switchFragment(EditProfile().newInstance(user))
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
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
            userProfile.name = obj.getJSONObject(i).getString("u_name")
            SharedPref.userName = userProfile.name.toString()

            if (obj.getJSONObject(i).getString("u_place") == "null") {
                userProfile.address = ""
            }
            else {
                userProfile.address = obj.getJSONObject(i).getString("u_place")
            }

            if (obj.getJSONObject(i).getString("u_exp") == "null") {
                userProfile.experience = ""
            }
            else {
                userProfile.experience = obj.getJSONObject(i).getString("u_exp")
            }

            if (obj.getJSONObject(i).getString("u_pic") == "null") {
                userProfile.picture = ""
            }
            else {
                userProfile.picture = obj.getJSONObject(i).getString("u_pic")
            }
        }
        return userProfile
    }

    private fun downloadProfilePic(url : String, imageName : String) {
        if (PermissionChecker.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                PermissionChecker.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        file = File(Environment.getExternalStorageDirectory().path + "/" + imageName)

        if (file.exists()) {
            val uri = Uri.fromFile(file)

            spDialog.showDialog()

            //load image from phone storage
            Picasso.get()
                    .load(uri)
                    .into(view!!.profile_show_photo)

            spDialog.hideDialog()
        }
        else {
            spDialog.showDialog()

            //get image from server and show it out
            Picasso.get()
                    .load(url)
                    .into(view!!.profile_show_photo)

            spDialog.hideDialog()

            //save to phone storage
            Picasso.get()
                    .load(url)
                    .into(JExtension.getTarget(imageName))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUserProfile(userProfile: UserData) {
        view!!.profile_show_name.text = "Name: " + SharedPref.userName
        view!!.profile_show_email.text = "Email: " + SharedPref.userEmail
        view!!.profile_show_place.text = "Address: " + userProfile.address

        if (SharedPref.userType == "Student") {
            view!!.profile_show_exp.text = getString(R.string.EduLevel) + userProfile.experience
        }
        else {
            view!!.profile_show_exp.text = getString(R.string.TeachExp) + userProfile.experience
        }

        if (userProfile.picture !== "") {
            val location = URLEndpoint.urlLoadImage + userProfile.picture!!.substring(9, userProfile.picture!!.length)
            val imageName = "MeMoRiZ/Profile/" + userProfile.picture!!.substring(9, userProfile.picture!!.length)
            downloadProfilePic(location, imageName)
        }

        user = userProfile
    }
}