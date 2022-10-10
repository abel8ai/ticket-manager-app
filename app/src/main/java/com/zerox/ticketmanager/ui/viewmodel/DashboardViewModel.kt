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
    private val _ticketsModel = MutableLiveData<MutableList<TicketEntity>>()
    val ticketsModel: LiveData<MutableList<TicketEntity>> get() = _ticketsModel
    private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
    private val _ticketModel = MutableLiveData<TicketEntity>()
    val ticketModel: LiveData<TicketEntity> get() = _ticketModel
    suspend fun getAllTickets() {
        _ticketsModel.postValue(repository.getAllTickets())
    }

    suspend fun addTicket(ticket: TicketEntity) {
        repository.addTicket(ticket)
        _ticketsModel.postValue(repository.getAllTickets())
    }

    suspend fun getLastTicketCreated() {
        _ticketModel.postValue(repository.getLastTicketCreated())
    }

    fun createGoogleCalendarEvent(credential: GoogleCredential) {
        // Initialize Calendar service with valid OAuth credentials
        val service =
            Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Ticket Manager")
                .build()

        // Create and initialize a new event
        val event = Event()
        event.summary = "Appointment"
        event.location = "Somewhere"

        // add attendees to event
        val attendees = ArrayList<EventAttendee>()
        attendees.add(EventAttendee().setEmail("attendeeEmail"))
        event.attendees = attendees

        // set event date
        val startDate = DateTime("2022-10-10T09:00:00-07:00")
        val endDate = DateTime("2022-10-10T09:00:00-07:00")

        event.start = EventDateTime()
            .setDateTime(startDate)
            .setTimeZone("UTC-4")

        event.end = EventDateTime()
            .setDateTime(endDate)
            .setTimeZone("UTC-4")

        // Insert the new event
        service.events().insert("primary", event).execute()
    }
}