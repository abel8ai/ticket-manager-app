package com.zerox.ticketmanager.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zerox.ticketmanager.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}