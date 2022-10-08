package com.zerox.ticketmanager

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.zerox.ticketmanager.databinding.ActivityLoginBinding
import com.zerox.ticketmanager.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    // inject the login viewmodel into the activity
    private val loginViewModel: LoginViewModel by viewModels()

    // viewBinding
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialization of the viewbinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // onClick listener for the Login Button
        binding.btnLogin.setOnClickListener {
            // obtains username and password enter by user
            val mUsername = binding.etUsername.text.toString()
            val mPassword = binding.etPassword.text.toString()

            // empty fields verification
            if (mUsername.isEmpty() || mPassword.isEmpty()) {
                val hint = resources.getString(R.string.mandatory_field)
                if (mUsername.isEmpty()){
                    binding.etUsername.hint = hint
                    binding.etUsername.setHintTextColor(Color.RED)
                }
                if (mPassword.isEmpty()){
                    binding.etPassword.hint = hint
                    binding.etPassword.setHintTextColor(Color.RED)
                }
            } else
            // launching the suspend fun from Coroutine
                CoroutineScope(Dispatchers.IO).launch {
                    loginViewModel.doLogin(mUsername, mPassword)
                }

        }
    }
}