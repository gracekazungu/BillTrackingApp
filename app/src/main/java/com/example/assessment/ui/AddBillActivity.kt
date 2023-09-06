package com.example.assessment.ui//package com.example.assessment.ui


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.assessment.R
import com.example.assessment.databinding.ActivityAddBillBinding
import com.example.assessment.model.Bill
import com.example.assessment.utils.Constants
import com.example.assessment.viewmodel.BillsViewModel
import java.util.Calendar
import java.util.UUID


class AddBillActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBillBinding
    val billsViewModel: BillsViewModel by viewModels()
    var selectedDate = 0
    var selectedMonth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBillBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupFreqSpinner()
        binding.btnSaveBill.setOnClickListener {
            validateBill()
        }
    }

    fun setupFreqSpinner() {
        val frequencies =
            arrayOf(Constants.WEEKLY, Constants.MONTHLY, Constants.QUARTERLY, Constants.ANNUAL)
        val freqAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, frequencies)
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spfrequency.adapter = freqAdapter
        binding.spfrequency.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (binding.spfrequency.selectedItem.toString()) {
                    Constants.WEEKLY -> {
                        showSpinner()
                        setUpDueDateSpinner(Array(7) { it + 1 })
                    }

                    Constants.MONTHLY -> {
                        showSpinner()
                        setUpDueDateSpinner(Array(31) { it + 1 })
                    }

                    Constants.QUARTERLY -> {
                        showSpinner()
                        setUpDueDateSpinner(Array(90) { it + 1 })
                    }

                    Constants.ANNUAL -> {
                        showDatePicker()
                        setupDpDueDate()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun showSpinner() {
        binding.dpDueDateAnnual.hide()
        binding.spduedate.show()
    }

    fun showDatePicker() {
        binding.dpDueDateAnnual.show()
        binding.spduedate.hide()
    }

    fun setUpDueDateSpinner(dates: Array<Int>) {
        val dueDateAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, dates)
        dueDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spduedate.adapter = dueDateAdapter
    }

    fun setupDpDueDate() {
        val cal = Calendar.getInstance()
        binding.dpDueDateAnnual.init(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ) { _, _, month, date ->
            selectedDate = date
            selectedMonth = month + 1
        }
    }

    fun validateBill() {
        val name = binding.etName.text.toString()
        val amount = binding.etAmount.text.toString()
        val frequency = binding.spfrequency.selectedItem.toString()
        val dueDate = if (frequency == Constants.ANNUAL) {
            "$selectedDate/$selectedMonth"
        } else {
            binding.spduedate.selectedItem.toString()
        }
        var error = false
        if (name.isBlank()) {
            error = true
            binding.etName.error = "name is required"
        }
        if (amount.isBlank()) {
            error = true
            binding.etName.error = "amount is required"
        }
        if (!error) {
            val prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
            val userId = prefs.getString(Constants.USER_ID, Constants.EMPTY_STRING)
            val newBill = Bill(
                name = name,
                amount = amount.toDouble(),
                frequency = frequency,
                dueDate = dueDate,
                billId = UUID.randomUUID().toString(),
                userId = userId.toString()
            )
            billsViewModel.saveBill(newBill)

            clearForm()
            finish()
            navigateToSummaryFragment()
        }
    }

    fun clearForm() {
        binding.etName.setText(Constants.EMPTY_STRING)
        binding.etAmount.setText(Constants.EMPTY_STRING)
        binding.spfrequency.setSelection(0)
        showSpinner()
        binding.spduedate.setSelection(0)
    }

    private fun navigateToSummaryFragment() {
        val fragment = SummaryFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import androidx.activity.viewModels
//import com.example.assessment.R
//import com.example.assessment.databinding.ActivityAddBillBinding
//import com.example.assessment.model.Bill
//import com.example.assessment.utils.Constants
//import com.example.assessment.viewmodel.BillsViewModel
//import java.util.Calendar
//import java.util.UUID
//
//class AddBillActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityAddBillBinding
//    private val billsViewModel: BillsViewModel by viewModels()
//
////    val selectedDate = 0
////    val selectedMonth = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAddBillBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupFreqSpinner()
//        setupDueDateSpinner()
//
//        binding.btnSaveBill.setOnClickListener {
//            val selectedFrequency = binding.spfrequency.selectedItem.toString()
//            val billName = binding.etName.text.toString()
//            val billAmount = binding.etAmount.text.toString().toDouble()
//            val selectedDueDate: String = when (selectedFrequency) {
//                "Annual" -> {
//                    val datePicker = binding.dpDueDateAnnual
//                    "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}"
//                }
//                else -> binding.spduedate.selectedItem.toString()
//            }
//
//            val bill = Bill(
//                billId = UUID.randomUUID().toString(),
//                name = billName,
//                amount = billAmount,
//                frequency = selectedFrequency,
//                dueDate = selectedDueDate,
//                userId = "USER_ID"
//            )
//
//            billsViewModel.saveBill(bill)
//
//            finish()
//            navigateToSummaryFragment()
//        }
//    }
//
//    private fun setupFreqSpinner() {
//        val adapter = ArrayAdapter.createFromResource(
//            this, R.array.frequencies, android.R.layout.simple_spinner_item
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spfrequency.adapter = adapter
//    }
//
//    private fun setupDueDateSpinner() {
//        binding.spfrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedFrequency = binding.spfrequency.selectedItem.toString()
//
//                if (selectedFrequency == "Annual") {
//                    binding.spduedate.visibility = View.GONE
//                    binding.dpDueDateAnnual.visibility = View.VISIBLE
//                } else {
//                    binding.dpDueDateAnnual.visibility = View.GONE
//                    binding.spduedate.visibility = View.VISIBLE
//
//                    val dueDateAdapter = when (selectedFrequency) {
//                        "Monthly" -> {
//                            val daysInMonth=Array(31){it+1}
////                            val daysInMonth = 1..31
//                            ArrayAdapter(this@AddBillActivity, android.R.layout.simple_spinner_item, daysInMonth.toList())
//                        }
//                        "Quarterly" -> {
//                            val daysInQuarter=Array(90){it+1}
////                            val daysInQuarter = 1..90
//                            ArrayAdapter(this@AddBillActivity, android.R.layout.simple_spinner_item, daysInQuarter.toList())
//                        }
//                        else -> {
//                            ArrayAdapter(this@AddBillActivity, android.R.layout.simple_spinner_item, Array(7){it+1})
////                                arrayOf(1, 2, 3, 4, 5, 6, 7))
//                        }
//                    }
//
//                    dueDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                    binding.spduedate.adapter = dueDateAdapter
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }
//    }
//
//    private fun navigateToSummaryFragment() {
//        val fragment = SummaryFragment()
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
//}

