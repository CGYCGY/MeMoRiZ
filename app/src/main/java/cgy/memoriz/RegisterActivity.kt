package cgy.memoriz

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        sharedPref.init(this)

        register_userName.validate(arrayOf({ s -> s.isOnlyLetterOrDigit()}, { s -> s.isLengthAtLeast(6)}),
                arrayOf("Username only accept letter and digit", "Username must be at least 6 character"), sharedPref, 1)
        register_userPass.validate(arrayOf({ s -> s.isLengthAtLeast(6)},{ s -> s.isPassMix()}),
                arrayOf("Password must be at least 6 character", "Password must contain number, uppercase and lowercase letter"), sharedPref, 2)
        register_userEmail.validate(arrayOf({ s -> s.isEmailValid()}),
                arrayOf("Valid email address required"), sharedPref, 3)

//      set what happen after user click the register button
        register_registerBtn.setOnClickListener {
            Log.d("check error exist", checkErrorExist(sharedPref).toString())
            if (checkErrorExist(sharedPref)) register()
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

                        if (obj.getString("message") == "User infomation added successfully") {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
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
                params["ad_pass"] = register_userPass.text.toString().encryptPass()

//                test = "DJpGiO0XE9pz3bwmIixr6cOFVaw4kMj6Uhhmroc5ayDMYK8="
//                test.decryptPass()

                params["ad_name"] = register_userName.text.toString()
                params["ad_email"] = register_userEmail.text.toString()
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}//Log.d("testing", params["ad_id"])