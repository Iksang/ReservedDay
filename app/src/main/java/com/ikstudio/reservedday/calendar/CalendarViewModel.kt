package com.ikstudio.reservedday.calendar

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.FileInputStream
import java.io.FileOutputStream

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    var diaryTextView : MutableLiveData<String> = MutableLiveData("")
    var contents : MutableLiveData<String> = MutableLiveData("")
    var contextEditText : MutableLiveData<String> = MutableLiveData("")
    var showDiaryTextView : MutableLiveData<Boolean> = MutableLiveData(false)
    var showContextEditText : MutableLiveData<Boolean> = MutableLiveData(false)
    var showDiaryContent : MutableLiveData<Boolean> = MutableLiveData(false)
    var showSaveBtn : MutableLiveData<Boolean> = MutableLiveData(false)
    var showUpdateBtn : MutableLiveData<Boolean> = MutableLiveData(false)
    var showDeleteBtn : MutableLiveData<Boolean> = MutableLiveData(false)
    val context = getApplication<Application>().applicationContext

    fun dateChange(year : Int, month : Int, dayOfMonth : Int){
        showDiaryTextView.value = true
        showSaveBtn.value = true
        showContextEditText.value = true
        showDiaryContent.value = false
        showUpdateBtn.value = false
        showDeleteBtn.value = false
        diaryTextView.value = String.format("%d / %d / %d", year, month+1, dayOfMonth)
        contextEditText.value = ""
        checkDay(year, month, dayOfMonth, userID)
    }

    fun onSaveBtnClick(){
        saveDiary(fname)
        showContextEditText.value = false
        showSaveBtn.value = false
        showUpdateBtn.value = true
        showDeleteBtn.value = true
        str = contextEditText.value?:""
        contents.value = str
        showDiaryContent.value = true
    }

    fun onUpdateBtnClick(){
        showContextEditText.value = true
        showDiaryContent.value = false
        contextEditText.value = str
        showSaveBtn.value = true
        showUpdateBtn.value = false
        showDeleteBtn.value = false
        contents.value = contextEditText.value?:""
    }

    fun onDeleteBtnClick(){
        showDiaryContent.value = false
        showUpdateBtn.value = false
        showDeleteBtn.value = false
        contextEditText.value = ""
        showContextEditText.value = true
        showSaveBtn.value = true
        removeDiary(fname)
    }

    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userId: String){
        //저장할 파일 이름설정
        fname = "" + userID + cYear + "-" + (cMonth+1) + "" + "-"+ cDay + ".txt"

        var fileInputStream: FileInputStream
        try{
            fileInputStream = context.openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)
            showContextEditText.value = false
            showDiaryContent.value = true
            contents.value = str
            showSaveBtn.value = false
            showUpdateBtn.value = true
            showDeleteBtn.value = true
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?){
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(readDay,
                AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS
            )
            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?){
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(readDay,
                AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS
            )
            val content = contextEditText.value?:""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}