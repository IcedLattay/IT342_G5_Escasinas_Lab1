package com.android.mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Session.currentUser

        if (user != null) {
            view.findViewById<TextView>(R.id.usernameTextView).text = user.username
            view.findViewById<TextView>(R.id.emailAddressTextView).text = user.emailAddress.maskEmail()
        }

        view.findViewById<MaterialButton>(R.id.logoutButton).setOnClickListener {
            val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            prefs.edit().remove("token").apply()

            Session.currentUser = null

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

            requireActivity().finish()
        }
    }

}

fun String.maskEmail(): String {
    val parts = this.split("@")
    if (parts.size != 2) return this

    val username = parts[0]
    val domain = parts[1]

    if (username.length <= 2) {
        return username.first() + "*@" + domain
    }

    val firstChar = username.first()
    val lastChar = username.last()
    val maskedMiddle = "*".repeat(username.length - 2)

    return "$firstChar$maskedMiddle$lastChar@$domain"
}