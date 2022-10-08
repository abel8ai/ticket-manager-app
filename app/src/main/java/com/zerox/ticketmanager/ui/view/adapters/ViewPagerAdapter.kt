package com.zerox.ticketmanager.ui.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zerox.ticketmanager.ui.view.fragments.*

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->
                OverviewFragment()
            1->
                WorkDetailsFragment()
            2->
                PurchasingFragment()
            3->
                FinishingUpFragment()
            4->
                PictureLogoFragment()
            else->
                Fragment()
        }
    }
}