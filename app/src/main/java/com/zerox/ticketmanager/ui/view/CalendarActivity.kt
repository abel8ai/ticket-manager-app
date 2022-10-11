package com.zerox.ticketmanager.ui.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.zerox.ticketmanager.databinding.ActivityCalendarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                widget.setDateSelected(date, false)
            } else
                widget.setDateSelected(date, true)
        }
    }
}