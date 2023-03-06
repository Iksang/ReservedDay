package com.ikstudio.reservedday.calendar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        binding.title.text = "달력 일기장"

        setListener()
        setObserve()
    }
    fun setListener(){
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            binding.diaryTextView.setText(String.format("%d / %d / %d", year, month+1, dayOfMonth))
            binding.contextEditText.setText("")
            calendarViewModel.checkDay(year, month, dayOfMonth, filesDir)
        }
    }

    fun setObserve(){
        calendarViewModel.statetype.observe(this){
            when(it){
                StateType.TodayInit -> setTodayInitState()
                StateType.ShowSchedule -> showScheduleState()
                StateType.Save -> setSaveBtnClick()
                StateType.Update -> onUpdateBtnClick()
                StateType.Delete -> onDeleteBtnClick()
                else -> Unit
            }
        }
    }

    fun setTodayInitState(){
        val today = Calendar.getInstance()
        binding.diaryTextView.setText(String.format("%d / %d / %d", today.get(Calendar.YEAR),
            today.get(Calendar.MONTH)+1, today.get(Calendar.DAY_OF_MONTH)))
        binding.contextEditText.setText("")
        calendarViewModel.checkDay( today.get(Calendar.YEAR),today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), filesDir)
    }

    fun showScheduleState(){
        binding.contextEditText.visibility = View.INVISIBLE
        binding.diaryContent.visibility = View.VISIBLE
        binding.saveBtn.visibility = View.INVISIBLE
        binding.updateBtn.visibility = View.VISIBLE
        binding.deleteBtn.visibility = View.VISIBLE
    }


    fun setSaveBtnClick(){
        calendarViewModel.saveDiary(binding.contextEditText.text.toString() , filesDir)
        binding.contextEditText.visibility = View.INVISIBLE
        binding.saveBtn.visibility = View.INVISIBLE
        binding.updateBtn.visibility = View.VISIBLE
        binding.deleteBtn.visibility = View.VISIBLE
        calendarViewModel.contents.value = binding.contextEditText.text.toString()
        binding.diaryContent.visibility = View.VISIBLE
    }

    fun onUpdateBtnClick(){
        binding.contextEditText.visibility = View.VISIBLE
        binding.diaryContent.visibility = View.INVISIBLE
        binding.contextEditText.setText( calendarViewModel.contents.value)
        binding.saveBtn.visibility = View.VISIBLE
        binding.updateBtn.visibility = View.INVISIBLE
        binding.deleteBtn.visibility = View.INVISIBLE
    }

    fun onDeleteBtnClick(){
        binding.diaryContent.visibility = View.INVISIBLE
        binding.updateBtn.visibility = View.INVISIBLE
        binding.deleteBtn.visibility = View.INVISIBLE
        binding.contextEditText.setText("")
        binding.contextEditText.visibility = View.VISIBLE
        binding.saveBtn.visibility = View.VISIBLE
        calendarViewModel.removeDiary(filesDir)
    }

}