package com.zerox.ticketmanager.ui.view.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zerox.ticketmanager.ui.view.fragments.*


class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,
                       private val ticketId: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when(position){
            0->{
                val bundle = Bundle()
                bundle.putInt("ticket_id", ticketId)
                fragment = OverviewFragment()
                fragment.arguments = bundle
            }

            1->{
                fragment = WorkDetailsFragment()
            }
            2->{
                fragment = PurchasingFragment()
            }
            3->{
                fragment = FinishingUpFragment()
            }
            4->{
                fragment = PictureFragment()
            }
        }
        return fragment
    }
}