package com.harshal.internshalatrainingsproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
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

class PasswordMessageActivity : AppCompatActivity() {

    lateinit var otp: TextView
    lateinit var password: TextView
    lateinit var cnfpwd : TextView
    lateinit var nextBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_message)

        otp = findViewById(R.id.etOTP)
        password = findViewById(R.id.etPassword)
        cnfpwd = findViewById(R.id.etCnfPassword)
        nextBtn = findViewById(R.id.btnNext)

        nextBtn.setOnClickListener {
            val otpText = otp.text.toString()
            val pwd = password.text.toString()
            val cnfPwd = cnfpwd.text.toString()
            val mobileNumber = intent.getStringExtra("Number")
            if(pwd.compareTo(cnfPwd,false) == 0){

                val intent = Intent(this, LoginActivity::class.java)
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result/"
                val jsonParams = JSONObject().put("mobile_number", mobileNumber).put("password",pwd).put("otp",otpText)
                if(ConnectionManager().checkConnectivity(this)){

                    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        try {
                            val res = it.getJSONObject("data")
                            val success = res.getBoolean("success")
                            if(success){
                                val trial = res.getString("successMessage")
                                if(trial.compareTo("Password has successfully changed.",false)==0){
                                    Toast.makeText(this, "Password has successfully changed.", Toast.LENGTH_SHORT).show()
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Enter Correct OTP! Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "Enter Correct OTP! Please try again.", Toast.LENGTH_SHORT).show()
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

            } else {
                Toast.makeText(this, "Passwords don't match!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
