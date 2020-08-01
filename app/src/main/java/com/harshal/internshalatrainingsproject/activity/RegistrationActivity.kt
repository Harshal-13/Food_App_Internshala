package com.harshal.internshalatrainingsproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.harshal.internshalatrainingsproject.R

class RegistrationActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobileNumber: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etCnfPassword: EditText
    lateinit var btnNext : Button
    lateinit var imgBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        title = "Registration Page"

        btnNext = findViewById(R.id.btnNext)
        imgBack = findViewById(R.id.imgBackArrow)
        etEmail = findViewById(R.id.etEmail)
        etName = findViewById(R.id.etName)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etCnfPassword = findViewById(R.id.etConfirmPwd)
        etMobileNumber = findViewById(R.id.etMobileNumber)

        btnNext.setOnClickListener {
            val mobileNumber = etMobileNumber.text.toString()
            val email = etEmail.text.toString()
            val name = etName.text.toString()
            val address = etAddress.text.toString()
            val pwd = etPassword.text.toString()
            val cnfPwd = etCnfPassword.text.toString()

            val intent = Intent(this, RegisterMessageActivity::class.java)
                .putExtra("MobileNumber", mobileNumber)
                .putExtra("Email",email)
                .putExtra("Name",name)
                .putExtra("Address",address)
                .putExtra("Password",pwd)
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
