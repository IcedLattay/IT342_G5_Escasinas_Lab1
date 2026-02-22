package com.android.mobile

import android.content.Context
import android.content.Intent
import org.json.JSONObject
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private val api by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameField = findViewById<EditText>(R.id.usernameEditText)
        val passwordField = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)
        val loginErrorText = findViewById<TextView>(R.id.loginErrorTextView)
        val navigateToSignUpButton = findViewById<MaterialButton>(R.id.navigateToSignupButton)

        fun onCreateLoginButton() {
            val watcher = object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    loginButton.isEnabled = usernameField.text.isNotBlank() && passwordField.text.isNotBlank()
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            usernameField.addTextChangedListener(watcher)
            passwordField.addTextChangedListener(watcher)
        }

        onCreateLoginButton()

        loginButton.setOnClickListener {

            loginButton.isEnabled = false

            val username = usernameField.text.toString()
            val password = passwordField.text.toString()


            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    api.login(LoginRequest(username, password))
                }

                if (response.isSuccessful) {
//                    Toast.makeText(this@LoginActivity, "Login Success!", Toast.LENGTH_SHORT).show()
                    val token = response.body()?.token
                    if (token != null) {
                        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
                        prefs.edit().putString("token", token).apply()
                    }

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
//                  Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()

                    loginButton.isEnabled = true

                    val errorBody = response.errorBody()?.string()

                    if (!errorBody.isNullOrEmpty()) {
                        val json = JSONObject(errorBody)
                        val message = json.getString("message")
                        loginErrorText.text = message
                    }

                    usernameField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                    passwordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                }
            }
        }

        navigateToSignUpButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }



}