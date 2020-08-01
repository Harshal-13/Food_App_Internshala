package com.harshal.internshalatrainingsproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.harshal.internshalatrainingsproject.R

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

            val intent = Intent(this, PasswordMessageActivity::class.java).putExtra("MobileNumber", mobileNumber).putExtra("Email",email)
            startActivity(intent)
            finish()
        }

        imgBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
