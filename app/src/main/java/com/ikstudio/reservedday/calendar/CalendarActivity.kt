package com.ikstudio.reservedday.calendar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ikstudio.reservedday.R
import com.ikstudio.reservedday.databinding.ActivityCalendarBinding
import java.io.FileInputStream
import java.io.FileOutputStream

class CalendarActivity : AppCompatActivity() {
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    lateinit var binding: ActivityCalendarBinding
    val calendarViewModel:CalendarViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        binding.viewModel = calendarViewModel
        binding.lifecycleOwner = this

        binding.title.text = "달력 일기장"

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendarViewModel.dateChange(year, month, dayOfMonth)
        }
    }

}