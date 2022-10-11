package com.zerox.ticketmanager.ui.view


import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.databinding.ActivityCalendarBinding
import com.zerox.ticketmanager.ui.view.adapters.TicketAdapter
import com.zerox.ticketmanager.ui.view.adapters.TicketReducedAdapter
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
    private var ticketList = emptyList<TicketEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarViewModel.alltickets.observe(this) {
            for (ticket in it) {
                val day = ticket.date.split("/")[0].toInt()
                val month = ticket.date.split("/")[1].toInt()
                val year = ticket.date.split("/")[2].toInt()
                val calendarDay = CalendarDay.from(year, month, day)
                binding.calendarView.setDateSelected(calendarDay, true)
            }

        }
        calendarViewModel.ticketsByDate.observe(this) {
            ticketList = it
            initRecyclerView()
        }

        CoroutineScope(Dispatchers.IO).launch {
            calendarViewModel.getAllTickets()
        }

        binding.calendarView.setAllowClickDaysOutsideCurrentMonth(false)
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (!selected) {
                widget.setDateSelected(date, true)
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        calendarViewModel.getTicketsByDate(date)
                    }
                } catch (exception: Exception) {
                    Toast.makeText(this@CalendarActivity, "dfsdf", Toast.LENGTH_SHORT)
                }
            } else {
                widget.setDateSelected(date, false)
                val adapter = TicketAdapter(emptyList())
                binding.rvTickets.adapter = adapter
            }
        }
    }

    private fun initRecyclerView() {
        val adapter = TicketReducedAdapter(ticketList)
        binding.rvTickets.layoutManager = LinearLayoutManager(this)
        binding.rvTickets.adapter = adapter
    }
}