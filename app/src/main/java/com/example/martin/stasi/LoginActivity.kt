package com.example.martin.stasi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        enterButton.setOnClickListener {
            if (userName.text.isNotEmpty()){
                val intent = Intent(this@LoginActivity, MainActivity::class.java )
                intent.putExtra("username", userName.text.toString())
                startActivity(intent)
            }
        }
    }
}


