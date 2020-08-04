package com.harshal.internshalatrainingsproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegisterYourself: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_login)

        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        title = "Log In"

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegisterYourself = findViewById(R.id.txtRegister)

        btnLogin.setOnClickListener {

            val mobileNumber = etMobileNumber.text.toString()
            val password = etPassword.text.toString()

            val intent = Intent(this@LoginActivity, MainActivity::class.java)

            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/login/fetch_result/"
            val jsonParams = JSONObject().put("mobile_number", mobileNumber).put("password",password)
            if(ConnectionManager().checkConnectivity(this)){

                val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val res = it.getJSONObject("data")
                        val success = res.getBoolean("success")
                        if(success){
                            val data = res.getJSONObject("data")
                            savePreferences(data)
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "Login Error! Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException){
                        Toast.makeText(this, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                        Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String> ()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "64dd3ad5877c86"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("ERROR")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Setting"){text, listener ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit"){text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create().show()
            }

        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, PasswordActivity::class.java)
            startActivity(intent)
        }

        txtRegisterYourself.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    fun savePreferences(data: JSONObject) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
        sharedPreferences.edit().putString("name",data.getString("name")).apply()
        sharedPreferences.edit().putString("email",data.getString("email")).apply()
        sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
        sharedPreferences.edit().putString("address",data.getString("address")).apply()
    }
}
