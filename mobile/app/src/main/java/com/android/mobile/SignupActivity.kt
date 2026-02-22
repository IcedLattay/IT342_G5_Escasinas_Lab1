package com.android.mobile

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.animation.doOnEnd
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    private val api by lazy { RetrofitClient.instance.create(ApiService::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val backButton = findViewById<LinearLayout>(R.id.backButton)

        backButton.setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundResource(R.drawable.back_button_bg_onpress)
                    v.animate()
                        .scaleX(0.92f)
                        .scaleY(0.92f)
                        .alpha(0.7f)
                        .setDuration(150)
                        .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    v.setBackgroundResource(R.drawable.back_button_bg)
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(150)
                        .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                        .withEndAction {
                            if (event.action == MotionEvent.ACTION_UP) {
                                finish()
                            }
                        }
                        .start()
                }
                MotionEvent.ACTION_CANCEL -> {

                }
            }

            true
        }

        val signupButton = findViewById<MaterialButton>(R.id.signupButton)
        var usernameIsValid = false
        var emailIsValid = false
        var passwordIsValid = false
        var confirmPasswordIsValid = false

        fun toggleButton() {
            if (!(usernameIsValid && emailIsValid && passwordIsValid && confirmPasswordIsValid)) {
                signupButton.isEnabled = false
            } else {
                signupButton.isEnabled = true
            }
        }


//        FOR USERNAMEEE
        var usernameLengthIsValid = false
        var usernameCharsIsValid = false
        val usernameContainer = findViewById<LinearLayout>(R.id.usernameFieldContainer)
        val usernameField = findViewById<EditText>(R.id.usernameEditText)
        val usernameErrorMessage = findViewById<TextView>(R.id.usernameErrorTextView)
        val usernameRulesContainer = findViewById<LinearLayout>(R.id.usernameRulesContainer)
        val lengthRule = findViewById<TextView>(R.id.usernameRule1)
        val lengthRuleCheckmark = findViewById<ImageView>(R.id.usernameRuleCheckmark1)
        val validCharsRule = findViewById<TextView>(R.id.usernameRule2)
        val validCharsRuleCheckmark = findViewById<ImageView>(R.id.usernameRuleCheckmark2)

        usernameField.setOnFocusChangeListener { _, hasFocus ->
            TransitionManager.beginDelayedTransition(usernameContainer)

            if (hasFocus) {
                usernameErrorMessage.visibility = View.GONE
                usernameRulesContainer.visibility = View.VISIBLE
            } else {
                usernameRulesContainer.visibility = View.GONE

                if (usernameIsValid) {
                    usernameErrorMessage.visibility = View.GONE
                } else {
                    usernameField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                    usernameErrorMessage.visibility = View.VISIBLE

                    if (usernameField.text.toString().isEmpty()) {
                        usernameErrorMessage.text = "This input is required"
                    } else if (!usernameIsValid) {
                        usernameErrorMessage.text = "Invalid username"
                    }
                }
            }
        }

        usernameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val username = usernameField.text.toString()

                if (username.isEmpty()) {
                    lengthRule.setTextColor(resources.getColor(R.color.rule_static, null))
                    lengthRuleCheckmark.visibility = View.GONE
                    validCharsRule.setTextColor(resources.getColor(R.color.rule_static, null))
                    validCharsRuleCheckmark.visibility = View.GONE
                    usernameIsValid = false
                } else {
                    if (username.length in 3..20) {
                        lengthRule.setTextColor(Color.BLACK)
                        lengthRuleCheckmark.visibility = View.VISIBLE
                        usernameLengthIsValid = true
                    } else {
                        lengthRule.setTextColor(resources.getColor(R.color.rule_invalid, null))
                        lengthRuleCheckmark.visibility = View.GONE
                        usernameField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                        usernameLengthIsValid = false
                    }

                    val validChars = username.matches("^[a-zA-Z0-9_]*$".toRegex())
                    if (validChars) {
                        validCharsRule.setTextColor(Color.BLACK)
                        validCharsRuleCheckmark.visibility = View.VISIBLE
                        usernameCharsIsValid = true
                    } else {
                        validCharsRule.setTextColor(resources.getColor(R.color.rule_invalid, null))
                        validCharsRuleCheckmark.visibility = View.GONE
                        usernameField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                        usernameCharsIsValid = false
                    }

                    usernameIsValid = usernameCharsIsValid && usernameLengthIsValid
                    if (usernameIsValid) usernameField.setBackgroundResource(R.drawable.edittext_bg)
                }

                toggleButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })


//        FOR EMAILLLLL
        val emailContainer = findViewById<LinearLayout>(R.id.emailFieldContainer)
        val emailField = findViewById<EditText>(R.id.emailAddressEditText)
        val emailErrorMessage = findViewById<TextView>(R.id.emailErrorTextView)

        val emailWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = emailField.text.toString()

                if (email.isEmpty()) {
                    emailIsValid = false
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailErrorMessage.visibility = View.GONE
                        emailField.setBackgroundResource(R.drawable.edittext_bg)
                        emailIsValid = true
                    } else {
                        emailErrorMessage.visibility = View.VISIBLE
                        emailErrorMessage.text = "Invalid email address"
                        emailField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                        emailIsValid = false
                    }
                }

                toggleButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        emailField.setOnFocusChangeListener { _, hasFocus ->
            TransitionManager.beginDelayedTransition(emailContainer)

            if (hasFocus) {
                emailField.addTextChangedListener(emailWatcher)
            } else {
                emailField.removeTextChangedListener(emailWatcher)

                if (emailField.text.toString().isEmpty()) {
                    emailErrorMessage.visibility = View.VISIBLE
                    emailErrorMessage.text = "This input is required"
                    emailField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                } else {
                    emailErrorMessage.visibility = View.GONE
                }
            }
        }




//        FOR PASSWORD && CONFIRM PASSWORD

        // password components
        var passwordLengthIsValid = false
        val passwordContainer = findViewById<LinearLayout>(R.id.passwordFieldContainer)
        val passwordField = findViewById<EditText>(R.id.passwordEditText)
        val passwordErrorMessage = findViewById<TextView>(R.id.passwordErrorTextView)
        val passwordRulesContainer = findViewById<LinearLayout>(R.id.passwordRulesContainer)
        val passwordLengthRule = findViewById<TextView>(R.id.passwordRule1)
        val passwordLengthRuleCheckmark = findViewById<ImageView>(R.id.passwordRuleCheckmark1)
        val passwordValidCharsRule1 = findViewById<TextView>(R.id.passwordRule2)
        val passwordValidCharsRuleCheckmark1 = findViewById<ImageView>(R.id.passwordRuleCheckmark2)
        val passwordValidCharsRule2 = findViewById<TextView>(R.id.passwordRule3)
        val passwordValidCharsRuleCheckmark2 = findViewById<ImageView>(R.id.passwordRuleCheckmark3)

        // confirm password components
        val confirmPasswordContainer = findViewById<LinearLayout>(R.id.confirmPasswordFieldContainer)
        val confirmPasswordField = findViewById<EditText>(R.id.confirmPasswordEditText)
        val confirmPasswordErrorMessage = findViewById<TextView>(R.id.confirmPasswordTextView)


        passwordField.setOnFocusChangeListener { _, hasFocus ->
            TransitionManager.beginDelayedTransition(passwordContainer)

            if (hasFocus) {
                passwordErrorMessage.visibility = View.GONE
                passwordRulesContainer.visibility = View.VISIBLE
            } else {
                passwordRulesContainer.visibility = View.GONE

                if (passwordIsValid) {
                    passwordErrorMessage.visibility = View.GONE
                } else {
                    passwordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                    passwordErrorMessage.visibility = View.VISIBLE

                    if (passwordField.text.toString().isEmpty()) {
                        passwordErrorMessage.text = "This input is required"
                    } else if (!passwordIsValid) {
                        passwordErrorMessage.text = "Invalid password"
                    }
                }

                val password = passwordField.text.toString()
                val confirmPassword = confirmPasswordField.text.toString()
                if (password != confirmPassword && confirmPassword.isNotEmpty()) {
                    confirmPasswordErrorMessage.visibility = View.VISIBLE
                    confirmPasswordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                    confirmPasswordErrorMessage.text = "This input does not match your password"
                }
            }
        }

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = passwordField.text.toString()
                val confirmPassword = confirmPasswordField.text.toString()

                if (password.isEmpty()) {
                    passwordLengthRule.setTextColor(resources.getColor(R.color.rule_static, null))
                    passwordLengthRuleCheckmark.visibility = View.GONE
                    passwordValidCharsRule1.setTextColor(resources.getColor(R.color.rule_static, null))
                    passwordValidCharsRuleCheckmark1.visibility = View.GONE
                    passwordValidCharsRule2.setTextColor(resources.getColor(R.color.rule_static, null))
                    passwordValidCharsRuleCheckmark2.visibility = View.GONE
                    passwordIsValid = false
                } else {
                    if (password.length >= 7) {
                        passwordLengthRule.setTextColor(Color.BLACK)
                        passwordLengthRuleCheckmark.visibility = View.VISIBLE
                        passwordLengthIsValid = true
                    } else {
                        passwordLengthRule.setTextColor(resources.getColor(R.color.rule_invalid, null))
                        passwordLengthRuleCheckmark.visibility = View.GONE
                        passwordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                        passwordIsValid = false
                    }

                    val hasLowerCase = password.any { it.isLowerCase() }
                    val hasUpperCase = password.any { it.isUpperCase() }
                    val hasNumbers = password.any { it.isDigit() }

                    if (hasLowerCase && hasUpperCase) {
                        passwordValidCharsRule1.setTextColor(Color.BLACK)
                        passwordValidCharsRuleCheckmark1.visibility = View.VISIBLE
                    } else {
                        passwordValidCharsRule1.setTextColor(resources.getColor(R.color.rule_invalid, null))
                        passwordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                        passwordValidCharsRuleCheckmark1.visibility = View.GONE
                    }

                    if (hasNumbers) {
                        passwordValidCharsRule2.setTextColor(Color.BLACK)
                        passwordValidCharsRuleCheckmark2.visibility = View.VISIBLE
                    } else {
                        passwordValidCharsRule2.setTextColor(resources.getColor(R.color.rule_invalid, null))
                        passwordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                        passwordValidCharsRuleCheckmark2.visibility = View.GONE
                    }

                    passwordIsValid = hasLowerCase && hasUpperCase && hasNumbers && passwordLengthIsValid

                    if (passwordIsValid) passwordField.setBackgroundResource(R.drawable.edittext_bg)
                }

                if (password != confirmPassword) {
                    confirmPasswordIsValid = false
                } else {
                    confirmPasswordErrorMessage.visibility = View.GONE
                    confirmPasswordField.setBackgroundResource(R.drawable.edittext_bg)
                }

                toggleButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })




        val confirmPasswordWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirmPassword = confirmPasswordField.text.toString()
                val password = passwordField.text.toString()

                if (confirmPassword.isEmpty()) {
                    confirmPasswordIsValid = false
                } else {
                    if (confirmPassword == password) {
                        confirmPasswordErrorMessage.visibility = View.GONE
                        confirmPasswordIsValid = true
                    } else {
                        confirmPasswordErrorMessage.visibility = View.VISIBLE
                        confirmPasswordErrorMessage.text = "This input does not match your password"
                        confirmPasswordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                        confirmPasswordIsValid = false
                    }
                }

                if (confirmPasswordIsValid) confirmPasswordField.setBackgroundResource(R.drawable.edittext_bg)

                toggleButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        confirmPasswordField.setOnFocusChangeListener { _, hasFocus ->
            TransitionManager.beginDelayedTransition(confirmPasswordContainer)

            if (hasFocus) {
                val password = passwordField.text.toString()

                if (password.isEmpty()) {
                    confirmPasswordErrorMessage.visibility = View.VISIBLE
                    confirmPasswordField.setBackgroundResource(R.drawable.edittext_bg_w_error)
                    confirmPasswordErrorMessage.text = "Set a password first"
                } else {
                    confirmPasswordField.addTextChangedListener(confirmPasswordWatcher)
                }

            } else {
                confirmPasswordField.removeTextChangedListener(confirmPasswordWatcher)

                if (confirmPasswordField.text.toString().isEmpty()) confirmPasswordErrorMessage.text = "This input is required"
            }
        }


        signupButton.setOnClickListener {

            signupButton.isEnabled = false

            val username = usernameField.text.toString()
            val emailAddress = emailField.text.toString()
            val password = passwordField.text.toString()


            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    api.register(RegisterRequest(username, emailAddress, password))
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@SignupActivity, "Register Success!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    signupButton.isEnabled = true

                    val errorBody = response.errorBody()?.string()

                    if (!errorBody.isNullOrEmpty()) {
                        val json = JSONObject(errorBody)
                        val message = json.getString("message")
                        usernameErrorMessage.text = message
                    }
                }
            }
        }

    }


}