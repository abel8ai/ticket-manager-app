package com.zerox.ticketmanager.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.databinding.FragmentOverviewBinding
import com.zerox.ticketmanager.databinding.FragmentWorkDetailsBinding

class WorkDetailsFragment : Fragment() {
    private var _binding: FragmentWorkDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWorkDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
}