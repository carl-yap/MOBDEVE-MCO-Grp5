package com.mobdeve.s21.manipol.marion.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        signupButton = findViewById(R.id.buttonSignup)

        dbHelper = UserDatabaseHelper(this)

        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isUsernameTaken = dbHelper.checkUserExists(username)
            if (isUsernameTaken) {
                Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
            } else {
                val isInserted = dbHelper.insertUser(username, password)
                if (isInserted) {
                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
