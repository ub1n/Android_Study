package kr.ac.smu.cs.study

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.System.exit
import java.util.ArrayList
import kotlin.system.exitProcess

class RegisterActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_register_button.setOnClickListener{
            val email = register_email.text.toString()
            val pw = register_pw.text.toString()
            val pwCheck = register_pwCheck.text.toString()

            signUp(email,pw,pwCheck)
        }//버튼 누를때의 동작
    }
    fun signUp(email: String, pw: String, pwCheck: String) {
        val database: UserDatabase = UserDatabase.getInstance(this) //application context

        val userDao: UserDao=database.userDao
        val userList = ArrayList<User>()  //정보를 저장할 리스트
        val signUpThread = Thread { userList.addAll(userDao.findUser(email)) }
        //Thread 만들기, addAll로 전체 정보중에서 UserDao에 있는 finduser 쿼리문을 통해 현재 입력한 email과 같은 정보가 있다면 userList에 넣음
        signUpThread.start()

        try {
            signUpThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        if (userList.size != 0) {

            return Toast.makeText(this, "동일한 이메일의 계정이 존재합니다", Toast.LENGTH_LONG).show()

        }

        if (pw != pwCheck) {

            return Toast.makeText(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다", Toast.LENGTH_LONG).show()
        }

        val user = User(email, pw)
        Thread { database.userDao.insert(user) }.start() //email, pw 정보 저장
        val intent = Intent(this, LoginActivity::class.java) //로그인 액티비티로 돌아가기
        this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        this.finish()

    }

}
