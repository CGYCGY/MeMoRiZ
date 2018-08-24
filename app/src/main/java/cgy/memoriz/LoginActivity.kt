package cgy.memoriz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
//class LoginActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

//    private var typeSelected : String ?= null
//    private val type = arrayOf("Student", "Lecturer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        SharedPref.init(this)

//        login_type!!.onItemSelectedListener = this
//        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, type)
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
//        login_type!!.adapter = arrayAdapter

        login_loginBtn.setOnClickListener {
            login()
        }

        login_registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {

//      start the StringRequest to get message from php after POST data to the database
        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlLogin,
                Response.Listener<String> { response ->
                    try {
//                      get the feedback message from the php and show it on the app by using Toast
                        val obj = JSONObject(response)
                        val checkEmail = obj.getString("message").contains("Welcome", false)
                        val checkPassword = (obj.getString("password").decryptPass() == login_userPass.text.toString())
                        val userType = obj.getString("type")

                        Log.d("check email", checkEmail.toString())
                        Log.d("check password", checkPassword.toString())
                        Log.d("password keyin", login_userPass.text.toString())

                        if (checkEmail && checkPassword) {
                            Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()

                            SharedPref.userName = obj.getString("message").substring(8)
                            SharedPref.userEmail = login_userEmail.text.toString()
                            SharedPref.userType = obj.getString("type")
                            Log.d("user Name", SharedPref.userName)

                            val intent = Intent(this, MainMenuActivity::class.java)
                            startActivity(intent)
                        }
                        else if (checkEmail && !checkPassword) {
                            Toast.makeText(applicationContext, "Your password is incorrect", Toast.LENGTH_LONG).show()
                        }
                        else Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() }) {

            //          pack the registration info to POST it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["u_email"] = login_userEmail.text.toString()
//                params["ad_pass"] = login_userPass.text.toString().decryptPass()
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

//    override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
//        typeSelected = type[position]
//    }
//
//    override fun onNothingSelected(adapterView: AdapterView<*>) {
//
//    }
}
