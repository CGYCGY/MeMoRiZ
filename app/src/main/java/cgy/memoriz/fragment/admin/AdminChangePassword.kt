package cgy.memoriz.fragment.admin

import android.content.DialogInterface
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
import cgy.memoriz.data.UserData
import cgy.memoriz.encryptPass
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.DialogFactory
import cgy.memoriz.others.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_admin_uac_edit.view.*
import org.json.JSONException
import org.json.JSONObject

class AdminChangePassword : MainActivityBaseFragment() {

    private var dialogFactory = DialogFactory()

    fun newInstance(user: UserData): AdminChangePassword{
        val args = Bundle()
        args.putSerializable("user detail", user)
        val fragment = AdminChangePassword()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_uac_edit, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val user : UserData = bundle.getSerializable("user detail") as UserData

            setTitle(user.name.toString())

            view.unBanBtn.visibility = GONE
            view.banBtn.visibility = GONE

            view.changePassBtn.setOnClickListener {
                view.hideKeyboard()
                dialogFactory.createTwoButtonDialog(context!!, "ALERT!", "Do you want to change this user password?",
                        DialogInterface.OnClickListener { dialog, which -> update(user) }).show()
            }

        }else {
            Log.e("missing UserData", "AdminChangePassword got error!")
        }

        return view
    }

    private fun update(user: UserData) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateUserPassword,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Password updated successfully") {
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

                params["u_email"] = user.email.toString()
                params["u_pass"] = view?.uac_edit_password?.text.toString().encryptPass()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}