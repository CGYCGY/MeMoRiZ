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
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.DialogFactory
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.fragment_admin_uac_edit.view.*
import org.json.JSONException
import org.json.JSONObject

class AdminChangeBanStatus : MainActivityBaseFragment() {

    private var dialogFactory = DialogFactory()

    fun newInstance(user: UserData): AdminChangeBanStatus{
        val args = Bundle()
        args.putSerializable("user detail", user)
        val fragment = AdminChangeBanStatus()
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

            view.uac_edit_password.visibility = GONE
            view.changePassBtn.visibility = GONE

            view.unBanBtn.setOnClickListener {
                dialogFactory.createTwoButtonDialog(context!!, "ALERT!", "Do you want to unban this user?",
                        DialogInterface.OnClickListener { dialog, which -> update(user, 0) }).show()
            }

            view.banBtn.setOnClickListener {
                dialogFactory.createTwoButtonDialog(context!!, "ALERT!", "Do you want to unban this user?",
                        DialogInterface.OnClickListener { dialog, which -> update(user, 1) }).show()
            }

        }else {
            Log.e("missing UserData", "AdminChangeBanStatus got error!")
        }

        return view
    }

    private fun update(user: UserData, ban: Int) {
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUpdateUserBan,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Ban status updated successfully") {
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
                params["u_ban"] = ban.toString()

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}