package com.zerox.ticketmanager.ui.view

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.zerox.ticketmanager.databinding.ActivityCalendarBinding
import java.time.LocalDate
import java.util.*


class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val calendarDay = CalendarDay.from(2022,10,13)
        val calendarDay2 = CalendarDay.from(2022,10,14)
        binding.calendarView.setDateSelected(calendarDay,true)
        binding.calendarView.setDateSelected(calendarDay2,true)
    }
}