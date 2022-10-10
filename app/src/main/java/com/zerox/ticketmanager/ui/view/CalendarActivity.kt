package com.zerox.ticketmanager.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerox.ticketmanager.databinding.ActivityCalendarBinding
import sun.bob.mcalendarview.vo.DateData


class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val dates = mutableListOf<DateData>()
        dates.add(DateData(2022, 10, 20))
        dates.add(DateData(2022, 10, 22))
        val size = dates.size
        for (i in 0 until size) {
            //mark multiple dates with this code.
            binding.calendar.markDate(dates[i].year, dates[i].month, dates[i].day)
        }
    }
}