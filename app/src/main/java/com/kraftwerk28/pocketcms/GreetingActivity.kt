package com.kraftwerk28.pocketcms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.kraftwerk28.pocketcms.databinding.ActivityGreetingBinding
import kotlinx.android.synthetic.main.activity_greeting.*

class GreetingActivity : AppCompatActivity() {

    internal data class User(var email: String, var password: String)

    private lateinit var binding: ActivityGreetingBinding
    private var user = User("John", "Smith")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_greeting)
        binding.user = user

        doneButton.setOnClickListener {
            applyBinding()
            Toast.makeText(this@GreetingActivity, tMesg(user), Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun tMesg(user: User) = "Dear, ${user.email}, ur password is ${user.password}."

    override fun onStart() {
        super.onStart()
        textView2.text = intent.getStringExtra("title")
    }

    private fun applyBinding() {
        binding.apply {
            user?.email = emailEdit.text.toString()
            user?.password = passwordEdit.text.toString()
        }
    }
}
