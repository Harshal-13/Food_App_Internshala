package com.harshal.internshalatrainingsproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.harshal.internshalatrainingsproject.R

class RegisterMessageActivity : AppCompatActivity() {

    lateinit var text1: TextView
    lateinit var text2: TextView
    lateinit var text3: TextView
    lateinit var text4: TextView
    lateinit var text5: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_message)

        text1 = findViewById(R.id.txtNumberText)
        text2 = findViewById(R.id.txtEmailText)
        text3 = findViewById(R.id.txtNameText)
        text4 = findViewById(R.id.txtAddressText)
        text5 = findViewById(R.id.txtPasswordText)


        text1.text = intent.getStringExtra("MobileNumber")
        text2.text = intent.getStringExtra("Email")
        text3.text = intent.getStringExtra("Name")
        text4.text = intent.getStringExtra("Address")
        text5.text = intent.getStringExtra("Password")
    }
}
