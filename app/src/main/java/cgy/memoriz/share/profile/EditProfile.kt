package cgy.memoriz.share.profile

import android.os.Bundle
import android.text.Editable
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
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.json.JSONException
import org.json.JSONObject

class EditProfile : MainActivityBaseFragment() {

    fun newInstance(user: UserData): EditProfile{
        val args = Bundle()
        args.putSerializable("user detail", user)
        val fragment = EditProfile()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val user : UserData = bundle.getSerializable("user detail") as UserData

            if (SharedPref.userType == "Student") {
                view!!.profile_edit_exp_name.text = getString(R.string.EduLevel)
            }
            else {
                view!!.profile_edit_exp_name.text = getString(R.string.TeachExp)
            }

            view.profile_edit_name.text = Editable.Factory.getInstance().newEditable(user.name)
            view.profile_edit_exp.text = Editable.Factory.getInstance().newEditable(user.experience)
            view.profile_edit_place.text = Editable.Factory.getInstance().newEditable(user.address)

            setTitle("Edit User Profile")

            view.saveProfileBtn.setOnClickListener {
                update()
            }

        }else {
            Log.e("missing UserData", "EditProfile got error!")
        }

        return view
    }

    private fun update() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateUserProfile,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "User profile updated successfully") {
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

                params["u_email"] = SharedPref.userEmail
                params["u_name"] = view?.profile_edit_name?.text.toString()
                params["u_exp"] = view?.profile_edit_exp?.text.toString()
                params["u_place"] = view?.profile_edit_place?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun update2() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateUserProfile,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)

                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "User profile updated successfully") {
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

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["u_email"] = SharedPref.userEmail
                params["u_name"] = view?.profile_edit_name?.text.toString()
                params["U_exp"] = view?.profile_edit_exp?.text.toString()
                params["u_place"] = view?.profile_edit_place?.text.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}