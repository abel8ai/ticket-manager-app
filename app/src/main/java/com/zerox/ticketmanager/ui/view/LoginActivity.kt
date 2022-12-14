package com.zerox.ticketmanager.ui.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.zerox.ticketmanager.R
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

        // observer to receive the authenticated user and launch the dashboard screen
        loginViewModel.user.observe(this) {user ->
            val intent = Intent(this,DashBoardActivity::class.java)
            intent.putExtra("user_id",user.id)
            startActivity(intent)
            finish()
        }
        // create login user:peter password:123
        CoroutineScope(Dispatchers.IO).launch {
            loginViewModel.addDummyUser()
        }

        // onClick listener for the Login Button
        binding.btnLogin.setOnClickListener {
            // obtains username and password enter by user
            val mUsername = binding.etUsername.text.toString()
            val mPassword = binding.etPassword.text.toString()

            // empty fields verification
            if (mUsername.isEmpty() || mPassword.isEmpty()) {
                val hint = resources.getString(R.string.mandatory_field)
                if (mUsername.isEmpty()) {
                    binding.etUsername.hint = hint
                    binding.etUsername.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (mPassword.isEmpty()) {
                    binding.etPassword.hint = hint
                    binding.etPassword.setHintTextColor(resources.getColor(R.color.light_red))
                }
            } else{
                // start the login progress bar
                binding.btnLoginText.visibility = View.GONE
                binding.btnLoginPb.visibility = View.VISIBLE
                // launching the suspend fun from Coroutine
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        loginViewModel.doLogin(mUsername, mPassword)
                    } catch (exception: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.btnLoginText.visibility = View.VISIBLE
                            binding.btnLoginPb.visibility = View.GONE
                        }
                    }

                }
            }



        }
    }
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.dialog_exit_app))
        builder.setMessage(resources.getString(R.string.close_app_confirmation))
        builder.create()
        builder.setPositiveButton(resources.getString(R.string.accept)) { _, _ -> finishAffinity() }
        builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ -> dialog!!.dismiss() }
        builder.show()
    }
}