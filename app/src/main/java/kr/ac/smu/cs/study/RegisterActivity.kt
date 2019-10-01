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
        }
    }
    fun signUp(email: String, pw: String, pwCheck: String) {
        var database: UserDatabase = UserDatabase.getInstance(this)

        var userDao: UserDao=database.userDao
        val userList = ArrayList<User>()
        val signUpThread = Thread { userList.addAll(userDao.findUser(email)) }
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
        Thread { database.userDao.insert(user) }.start()
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        this.finish()

    }

}
