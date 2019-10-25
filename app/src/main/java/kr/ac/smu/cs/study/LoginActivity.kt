package kr.ac.smu.cs.study

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import java.util.ArrayList
import com.amitshekhar.DebugDB



class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DebugDB.getAddressLog()



        login_checkBox.setChecked(App.prefs.myCheckbox)
        login_checkId.setChecked(App.prefs.myCheckId)
        login_checkPw.setChecked(App.prefs.myCheckPw)
        if(login_checkId.isChecked){  //체크되어있으면 이메일 바로 띄워줌
            login_email.setText(App.prefs.loginId)
        }
        if(login_checkPw.isChecked){
            login_pw.setText(App.prefs.loginPw)
        }

        if((login_checkBox.isChecked==true)&&(App.prefs.loginId.length!=0)){ //자동로그인
            val email=App.prefs.loginId
            val pw=App.prefs.loginPw
            if(Login(email,pw)){
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                this.finish()

            } else {
                Toast.makeText(this, "일치하는 계정이 없습니다", Toast.LENGTH_LONG).show()
            }
        }
        login_checkBox.setOnClickListener{
            App.prefs.myCheckbox=login_checkBox.isChecked //체크박스 상태저장
        }
        login_checkId.setOnClickListener{
            App.prefs.myCheckId=login_checkId.isChecked   //이메일 체크박스 상태 저장
        }
        login_checkPw.setOnClickListener{
            App.prefs.myCheckPw=login_checkPw.isChecked  //비밀번호 체크박스 상태저장
        }
        login_login_button.setOnClickListener {
            App.prefs.loginId=login_email.text.toString()  //email 상태 저장
            App.prefs.loginPw=login_pw.text.toString() //pw 상태저장
            val email = login_email.text.toString()
            val pw = login_pw.text.toString()


            if (Login(email,pw)) {

                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                this.finish()

            } else {
                Toast.makeText(this, "일치하는 계정이 없습니다", Toast.LENGTH_LONG).show()
            }
        }
        login_register_button.setOnClickListener {

            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun Login(email: String, pw: String) :Boolean{

        val database: UserDatabase = UserDatabase.getInstance(this)
        val userDao: UserDao = database.userDao
        val userList = ArrayList<User>()
        val loginThread = Thread { userList.addAll(userDao.userLogin(email, pw)) }
        //입력한 email,pw의 정보를 가진 게 있는지 쿼리를 날리고 있다면 userList에 저장
        loginThread.start()

        try {
            loginThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return if (userList.size == 0) {
            false
        } else userList[0].email == email



    }

}
