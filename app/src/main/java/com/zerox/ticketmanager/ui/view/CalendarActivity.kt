package com.zerox.ticketmanager.ui.view


import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.zerox.ticketmanager.R
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

    // viewmodel injection
    private val calendarViewModel: CalendarViewModel by viewModels()

    // viewBinding
    private lateinit var binding: ActivityCalendarBinding

    // logged user's id
    // initualized in -1 to verify that the id got thru
    private var userId =-1

    // list of tickets to load tickets by date
    private var ticketList = emptyList<TicketEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getIntExtra("user_id",-1)

        // observer to receive tickets data when ready
        calendarViewModel.alltickets.observe(this) {
            // load all tickets into calendar view
            for (ticket in it) {
                val day = ticket.date.split("/")[0].toInt()
                val month = ticket.date.split("/")[1].toInt()
                val year = ticket.date.split("/")[2].toInt()
                val calendarDay = CalendarDay.from(year, month, day)
                binding.calendarView.setDateSelected(calendarDay, true)
            }

        }
        // support action bar configuration
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        supportActionBar!!.title = Html.fromHtml("<font color='#3DCB01'>Calendar</font>")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // observer to receive tickets data from a specific data
        calendarViewModel.ticketsByDate.observe(this) {
            // show al tickets from a specific date in recycler view
            ticketList = it
            initRecyclerView()
        }

        // running coroutine to get all tickets
        CoroutineScope(Dispatchers.IO).launch {
            try {
                calendarViewModel.getAllTicketsByUserId(userId)
            }catch (exception: Exception){ // do nothing

            }

        }

        // calendar view configuration
        binding.calendarView.setAllowClickDaysOutsideCurrentMonth(false)
        // listener to get date tickets when clicked
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (!selected) {
                widget.setDateSelected(date, true)

                // running coroutine to get tickets from specific date
                // throw NotTicketsInDateException if there are no tickets
                // it should no throw the exception given that there always gonna be an event if
                // the date is marked
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        calendarViewModel.getTicketsByDateAndUser(date,userId)
                    } catch (exception: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@CalendarActivity,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            } else {
                // clear the recycler view when clicking an unmarked date
                widget.setDateSelected(date, false)
                val adapter = TicketReducedAdapter(emptyList())
                binding.rvTickets.adapter = adapter
            }
        }
    }

    // initialization for the recycler view
    private fun initRecyclerView() {
        val adapter = TicketReducedAdapter(ticketList)
        binding.rvTickets.layoutManager = LinearLayoutManager(this)
        binding.rvTickets.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}