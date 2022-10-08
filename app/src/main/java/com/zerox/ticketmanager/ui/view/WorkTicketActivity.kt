package com.zerox.ticketmanager.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.databinding.ActivityWorkTicketBinding
import com.zerox.ticketmanager.databinding.DialogAddTicketBinding
import com.zerox.ticketmanager.ui.view.adapters.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkTicketActivity : AppCompatActivity() {
    // viewBinding
    private lateinit var binding: ActivityWorkTicketBinding
    // variable to get ticket id from extras
    private var ticketId = -1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // configure the support action bar's title and back button
        supportActionBar!!.title = resources.getString(R.string.work_ticket_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        ticketId = intent.extras!!.getInt("ticket_id")
        initTab()

    }

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
                4->
                    tab.setIcon(R.drawable.ic_camera_24)
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
                val intent = Intent(this,DirectionsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this,DashBoardActivity::class.java))
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        onSupportNavigateUp()
        super.onBackPressed()
    }
}