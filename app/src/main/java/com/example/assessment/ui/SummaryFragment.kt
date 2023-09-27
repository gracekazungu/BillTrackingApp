package com.example.assessment.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.assessment.R
import com.example.assessment.databinding.FragmentSummaryBinding
import com.example.assessment.viewmodel.BillsViewModel

class SummaryFragment : Fragment() {
    private var binding: FragmentSummaryBinding? = null

    private val billsViewModel: BillsViewModel by viewModels()
    private lateinit var adapter: BillAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BillAdapter(requireContext(), R.layout.item_bill, mutableListOf())
        binding?.listViewBills?.adapter = adapter

        billsViewModel.getAllBills().observe(viewLifecycleOwner,
            Observer{ bills ->
            adapter.clear()
            adapter.addAll(bills)
            adapter.notifyDataSetChanged()
        })

        binding?.fabAddBill?.setOnClickListener {
            startActivity(Intent(requireContext(), AddBillActivity::class.java))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        binding?.fabAddBill?.setOnClickListener {
            startActivity(Intent(requireContext(), AddBillActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
