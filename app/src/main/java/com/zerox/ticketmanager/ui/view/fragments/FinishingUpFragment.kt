package com.zerox.ticketmanager.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.databinding.FragmentFinishingUpBinding
import com.zerox.ticketmanager.databinding.FragmentOverviewBinding

class FinishingUpFragment : Fragment() {
    private var _binding: FragmentFinishingUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFinishingUpBinding.inflate(inflater,container,false)
        return binding.root
    }
}