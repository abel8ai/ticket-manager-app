package com.zerox.ticketmanager.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zerox.ticketmanager.databinding.FragmentPurchasingBinding

class PurchasingFragment : Fragment() {
    private var _binding: FragmentPurchasingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPurchasingBinding.inflate(inflater,container,false)
        return binding.root
    }
}