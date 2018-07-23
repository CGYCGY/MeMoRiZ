package cgy.memoriz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

//  declare first to used it later
    lateinit var registerBtn: Button
    lateinit var userID: EditText
    lateinit var userPassword: EditText
    lateinit var userName: EditText
    lateinit var userEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//      link the view by id
        registerBtn = findViewById(R.id.registerBtn)
        userID = findViewById(R.id.userID)
        userPassword = findViewById(R.id.userPassword)
        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)

//      set what happen after user click the register button
        registerBtn.setOnClickListener {
            register()
        }
    }

    private fun register() {
//      get the text from the view and change it to String variable
        val id = userID.text.toString()
        val pass = userPassword.text.toString()
        val name = userName.text.toString()
        val email = userEmail.text.toString()

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
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                    }
                }) {

//          pack the registration info to POSt it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["ad_id"] = id
                params["ad_pass"] = pass
                params["ad_name"] = name
                params["ad_email"] = email
//                Log.d("testing", params["ad_id"])
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}