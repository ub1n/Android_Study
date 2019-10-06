package kr.ac.smu.cs.study

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    val PREFS_FILENAME="prefs"
    val PREF_KEY_MY_EDITTEXT="loginId"
    val prefs:SharedPreferences=context.getSharedPreferences(PREFS_FILENAME,0)

    //sahredPreferences에 저장하려는 변수, get set은 추가 지정
    var loginId: String
        get()= prefs.getString(PREF_KEY_MY_EDITTEXT,"")!!
        set(value)=prefs.edit().putString(PREF_KEY_MY_EDITTEXT,value).apply()
    var myCheckbox: Boolean
        get()=prefs.getBoolean("myCheckbox",false)
        set(value)=prefs.edit().putBoolean("myCheckbox",value).apply()
    var loginPw: String
        get()=prefs.getString("loginPw","")!!
        set(value)=prefs.edit().putString("loginPw",value).apply()
    var myCheckId: Boolean
        get()=prefs.getBoolean("myCheckId",false)
        set(value)=prefs.edit().putBoolean("myCheckId",value).apply()
    var myCheckPw: Boolean
        get()=prefs.getBoolean("myCheckPw",false)
        set(value)=prefs.edit().putBoolean("myCheckPw",value).apply()

}