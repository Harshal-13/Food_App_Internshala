package com.harshal.internshalatrainingsproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.harshal.internshalatrainingsproject.R

class PasswordMessageActivity : AppCompatActivity() {

    lateinit var text1: TextView
    lateinit var text2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_message)

        text1 = findViewById(R.id.txtNumberText)
        text2 = findViewById(R.id.txtEmailText)

        text1.text = intent.getStringExtra("MobileNumber")
        text2.text = intent.getStringExtra("Email")
    }
}
