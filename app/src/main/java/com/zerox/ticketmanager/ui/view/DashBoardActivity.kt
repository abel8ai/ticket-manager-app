package com.zerox.ticketmanager.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zerox.ticketmanager.databinding.ActivityDashboardBinding
import com.zerox.ticketmanager.ui.utils.ViewAnimation

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var isRotate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialization for the menu elements
        ViewAnimation.init(binding.fabWorkTicket)
        ViewAnimation.init(binding.fabGetDirections)

        //on click to display menu elements with animation
        binding.fabMenu.setOnClickListener {
            isRotate = ViewAnimation.rotateFab(it, !isRotate)
            if(isRotate){
                ViewAnimation.showIn(binding.fabWorkTicket)
                ViewAnimation.showIn(binding.fabGetDirections)
            }else{
                ViewAnimation.showOut(binding.fabWorkTicket)
                ViewAnimation.showOut(binding.fabGetDirections)
            }
        }

    }
}