package com.harshal.internshalatrainingsproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class PasswordActivity : AppCompatActivity() {

    lateinit var etMobileNumber: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext : Button
    lateinit var imgBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        title = "Forget Password"

        btnNext = findViewById(R.id.btnNext)
        imgBack = findViewById(R.id.imgBackArrow)
        etEmail = findViewById(R.id.etEmail)
        etMobileNumber = findViewById(R.id.etMobileNumber)

        btnNext.setOnClickListener {
            val mobileNumber = etMobileNumber.text.toString()
            val email = etEmail.text.toString()

            val intent = Intent(this, PasswordMessageActivity::class.java).putExtra("Number",mobileNumber)
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"
            val jsonParams = JSONObject().put("mobile_number", mobileNumber).put("email",email)
            if(ConnectionManager().checkConnectivity(this)){

                val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val res = it.getJSONObject("data")
                        val success = res.getBoolean("success")
                        if(success){
                            val trial = res.getBoolean("first_try")
                            if(trial){
                                Toast.makeText(this, "OTP sent to registered Email address.", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Please use the previous OTP provided to registered Email address.", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "User not registered! Please try again.", Toast.LENGTH_SHORT).show()
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

        imgBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
