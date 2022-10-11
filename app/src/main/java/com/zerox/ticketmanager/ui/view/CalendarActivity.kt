package com.zerox.ticketmanager.ui.view


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.zerox.ticketmanager.databinding.ActivityCalendarBinding
import com.zerox.ticketmanager.ui.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarActivity : AppCompatActivity() {

    private val calendarViewModel: CalendarViewModel by viewModels()
    // viewBinding
    private lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarViewModel.alltickets.observe(this){
            for (ticket in it){
                val day = ticket.date.split("/")[0].toInt()
                val month = ticket.date.split("/")[1].toInt()
                val year = ticket.date.split("/")[2].toInt()
                val calendarDay = CalendarDay.from(year,month,day)
                binding.calendarView.setDateSelected(calendarDay,true)
            }

        }
        calendarViewModel.ticketsByDate.observe(this){

        }

        try {
            CoroutineScope(Dispatchers.IO).launch {
                calendarViewModel.getAllTickets()
            }
        }
        catch (exception:Exception){

        }
        binding.calendarView.setAllowClickDaysOutsideCurrentMonth(false)
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                widget.setDateSelected(date, false)
            } else
                widget.setDateSelected(date, true)
        }
    }
}