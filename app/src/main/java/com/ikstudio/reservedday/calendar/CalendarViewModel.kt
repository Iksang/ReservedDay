package com.ikstudio.reservedday.calendar

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class CalendarViewModel : ViewModel() {
    var userID: String = "userID"
    lateinit var fname: String
    var contents = MutableLiveData("")
    var editContents = MutableLiveData("")
    var statetype = MutableLiveData(StateType.TodayInit)

    fun stateRefresh(stateType: StateType){
        statetype.value = stateType
    }

    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, fileDir: File) {
        editContents.value = ""
        //저장할 파일 이름설정
        fname = "" + userID + cYear + "-" + (cMonth+1) + "" + "-"+ cDay + ".txt"
        println("fname = $fname")
        var str:String? = null
        try{
            File(fileDir, fname).inputStream().buffered().use{
                val fileData = ByteArray(it.available())
                it.read(fileData)
                str = String(fileData)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        stateRefresh(StateType.ShowSchedule)
        if(str!=null) contents.value = str else contents.value = ""
    }

    fun updateDiary(){
        editContents.value = contents.value
    }

    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(fileDir : File){
        editContents.value=""
        try {
//            )
            File(fileDir, fname).outputStream().buffered().use{
                it.write("".toByteArray())
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary(fileDir : File){
        try {
            File(fileDir, fname).outputStream().buffered().use{
                it.write((editContents.value?:"").toByteArray())
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        contents.value = editContents.value?:""
    }
}