package com.zerox.ticketmanager.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.databinding.FragmentOverviewBinding
import com.zerox.ticketmanager.ui.viewmodel.WorkTicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    //viewModel Injection
    private val workTicketViewModel by viewModels<WorkTicketViewModel> ()
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    private var ticketId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater,container,false)

        // obtain the ticket id from the activity
        ticketId = requireArguments().getInt("ticket_id")

        // observer to receive the ticket once is tha data is available
        workTicketViewModel.ticketModel.observe(requireActivity()){

            val ticket = resources.getString(R.string.ticket_no) + it.id.toString()
            binding.ticketNumber.text = ticket
            binding.tvClientName.text = it.clientName
            binding.tvPhone.text = it.phoneNumber
            binding.tvAddress.text = it.address
            binding.tvDate.text = it.date
        }
        CoroutineScope(Dispatchers.IO).launch {
            workTicketViewModel.getTicketById(ticketId)
        }

        return binding.root

    }
}