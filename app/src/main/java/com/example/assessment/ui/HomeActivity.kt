package com.example.assessment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.assessment.R
import com.example.assessment.databinding.ActivityHomeBinding
import com.example.assessment.viewmodel.BillsViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val billsViewModel: BillsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        billsViewModel.createRecurringBills()
    }

    override fun onResume() {
        super.onResume()
        setupBottomNav()
    }
    private fun setupBottomNav(){
       binding.bnvHome.setOnItemSelectedListener { menuItem->
           when(menuItem.itemId){
               R.id.summary->{
                   supportFragmentManager
                       .beginTransaction()
                       .replace(R.id.fcvHome,SummaryFragment()).commit()
                   true
               }

               R.id.upcoming->{
                   supportFragmentManager
                       .beginTransaction()
                       .replace(R.id.fcvHome,UpcomingBillsFragment()).commit()
                   true
               }

               R.id.paid->{
                   supportFragmentManager
                       .beginTransaction()
                       .replace(R.id.fcvHome,PaidBillsFragment()).commit()
                   true
               }

               R.id.settings->{
                   supportFragmentManager
                       .beginTransaction()
                       .replace(R.id.fcvHome,SettingsFragment()).commit()
                   true
               }
               else->{
                   false
               }
           }
       }
    }
}