package com.zerox.ticketmanager.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerox.ticketmanager.databinding.ActivityCalendarBinding


class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}