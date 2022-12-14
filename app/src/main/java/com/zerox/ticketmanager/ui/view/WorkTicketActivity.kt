package com.zerox.ticketmanager.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.databinding.ActivityWorkTicketBinding
import com.zerox.ticketmanager.databinding.DialogAddTicketBinding
import com.zerox.ticketmanager.ui.view.adapters.ViewPagerAdapter
import com.zerox.ticketmanager.ui.viewmodel.WorkTicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkTicketActivity : AppCompatActivity() {
    // viewBinding
    private lateinit var binding: ActivityWorkTicketBinding
    // viewmodel injection
    private val workTicketViewModel : WorkTicketViewModel by viewModels()
    private lateinit var ticket: TicketEntity
    // variable to get ticket id from extras
    private var ticketId = -1;
    private var userId = -1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set title and back function for the support action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24);
        supportActionBar!!.title = Html.fromHtml("<font color='#3DCB01'>Work Ticket</font>")

        ticketId = intent.extras!!.getInt("ticket_id")
        userId = intent.extras!!.getInt("user_id")
        initTab()

        // observer to receive the ticket once is tha data is available
        workTicketViewModel.ticket.observe(this) {
            ticket = it
        }
        // retrieve ticket from database
        CoroutineScope(Dispatchers.IO).launch {
            workTicketViewModel.getTicketById(ticketId)
        }
    }
    // initialize the tab layout with all the fragments
    private fun initTab(){
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle,ticketId)
        binding.vpSections.adapter = adapter

        TabLayoutMediator(binding.tlSections,binding.vpSections){tab,position->
            when(position){
                0->
                    tab.text = getText(R.string.overview)
                1->
                    tab.text = getText(R.string.work_details)
                2->
                    tab.text = getText(R.string.purchasing)
                3->
                    tab.text = getText(R.string.finishing_up)
                4->{
                    tab.setIcon(R.drawable.ic_camera_24)
                }

            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.work_ticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.go_to_dashboard ->{
                val intent = Intent(this,DashBoardActivity::class.java)
                startActivity(intent)
            }
            R.id.go_to_directions ->{
                val intent = Intent(this, DirectionsActivity::class.java)
                intent.putExtra("direction", ticket.address)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // back button in action bar
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this,DashBoardActivity::class.java)
        intent.putExtra("user_id",userId)
        startActivity(intent)
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        onSupportNavigateUp()
        super.onBackPressed()
    }
}