package cgy.memoriz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_userName.validate(arrayOf({ s -> s.isOnlyLetterOrDigit()}, { s -> s.isLengthAtLeast(6)}),
                arrayOf("Username only accept letter and digit", "Username must be at least 6 character"))
        register_userPass.validate(arrayOf({ s -> s.isLengthAtLeast(6)},{ s -> s.isPassMix()}),
                arrayOf("Password must be at least 6 character", "Password must contain number, uppercase and lowercase letter"))
        register_userEmail.validate(arrayOf({ s -> s.isEmailValid()}),
                arrayOf("Valid email address required"))

//      set what happen after user click the register button
        register_registerBtn.setOnClickListener {
            register()
        }
    }

    private fun register() {

//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlRegister,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() }) {

//          pack the registration info to POSt it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
//                params["ad_id"] = register_userID.text.toString()
                params["ad_pass"] = register_userPass.text.toString()
                params["ad_name"] = register_userName.text.toString()
                params["ad_email"] = register_userEmail.text.toString()
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}//Log.d("testing", params["ad_id"])