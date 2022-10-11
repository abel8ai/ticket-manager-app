package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    // live data to get all the tickets and notify the view
    private val _allTickets = MutableLiveData<MutableList<TicketEntity>>()
    val allTickets: LiveData<MutableList<TicketEntity>> get() = _allTickets

    // live data to get last ticket created the tickets and notify the view
    private val _ticket = MutableLiveData<TicketEntity>()
    val ticket: LiveData<TicketEntity> get() = _ticket

    // credentials for google calendar events
    private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()


    suspend fun getAllTickets() {
        _allTickets.postValue(repository.getAllTickets())
    }

    suspend fun addTicket(ticket: TicketEntity) {
        repository.addTicket(ticket)
        _allTickets.postValue(repository.getAllTickets())
    }

    suspend fun getLastTicketCreated() {
        _ticket.postValue(repository.getLastTicketCreated())
    }

    // function to create Google Calendar event
    // doesn't work for credential problems
    suspend fun createGoogleCalendarEvent(credential: GoogleCredential) {
        // Initialize Calendar service with valid OAuth credentials
        val service =
            Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Ticket Manager")
                .build()

        val ticketList = repository.getAllTickets()

        for (ticket in ticketList) {
            // Create and initialize a new event
            val event = Event()
            event.summary = ticket.motive
            event.location = ticket.address

            // add attendees to event
            val attendees = ArrayList<EventAttendee>()
            attendees.add(EventAttendee().setDisplayName(ticket.clientName))
            event.attendees = attendees

            // add time from ticket
            val day = ticket.date.split("/")[0].toInt()
            val month = ticket.date.split("/")[1].toInt()
            val year = ticket.date.split("/")[2].toInt()

            val time = ticket.time.split(" ")[0]
            val ampm = ticket.time.split(" ")[1]

            var hour = ""
            if (ampm == "am")
                hour = time.split(":")[0]
            else{
                var hourCalc = time.split(":")[0].toInt()+12
                hour = hourCalc.toString()
            }

            val minutes = ticket.time.split(":")[1]

            val startDate = DateTime("$year-$month-${day}T$hour:$minutes:00")
            val endDate = DateTime("$year-$month-${day}T${hour + 1}:$minutes:00")

            event.start = EventDateTime()
                .setDateTime(startDate)
                .setTimeZone("UTC-4")

            event.end = EventDateTime()
                .setDateTime(endDate)
                .setTimeZone("UTC-4")

            // Insert the new event
            service.events().insert("primary", event).execute()
        }


        // set event date

    }
}