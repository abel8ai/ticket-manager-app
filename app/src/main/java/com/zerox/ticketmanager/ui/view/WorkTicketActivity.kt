package com.zerox.ticketmanager.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.databinding.ActivityWorkTicketBinding
import com.zerox.ticketmanager.databinding.DialogAddTicketBinding

class WorkTicketActivity : AppCompatActivity() {
    // viewBinding
    private lateinit var binding: ActivityWorkTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // configure the support action bar's title and back button
        supportActionBar!!.title = resources.getString(R.string.work_ticket_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val id = intent.extras!!.get("ticket_id")
        binding.tv.text = id.toString()

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
                val intent = Intent(this,DirectionsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}