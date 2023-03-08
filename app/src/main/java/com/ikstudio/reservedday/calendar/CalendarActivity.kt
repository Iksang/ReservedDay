package com.ikstudio.reservedday.calendar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ikstudio.reservedday.R
import com.ikstudio.reservedday.databinding.ActivityCalendarBinding
import java.util.*

class CalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding
    val calendarViewModel:CalendarViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        binding.viewModel = calendarViewModel
        binding.lifecycleOwner = this
        setListener()
        setObserve()
    }
    fun setListener(){
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            binding.diaryTextView.setText(String.format("%d / %d / %d", year, month+1, dayOfMonth))
            calendarViewModel.checkDay(year, month, dayOfMonth, filesDir)
        }
    }

    fun setObserve(){
        calendarViewModel.statetype.observe(this){
            when(it){
                StateType.TodayInit -> setTodayInitState()
                StateType.ShowSchedule -> Unit
                StateType.Save -> calendarViewModel.saveDiary(filesDir)
                StateType.Update -> calendarViewModel.updateDiary()
                StateType.Delete ->  calendarViewModel.removeDiary(filesDir)
                else -> Unit
            }
        }
    }

    fun setTodayInitState(){
        val today = Calendar.getInstance()
        binding.diaryTextView.setText(String.format("%d / %d / %d", today.get(Calendar.YEAR),
            today.get(Calendar.MONTH)+1, today.get(Calendar.DAY_OF_MONTH)))
        calendarViewModel.checkDay( today.get(Calendar.YEAR),today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), filesDir)
    }
}