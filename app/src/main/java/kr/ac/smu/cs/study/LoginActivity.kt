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

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_login_button.setOnClickListener{

            val email=login_email.text.toString()
            val pw=login_pw.text.toString()

            Login(email,pw)
        }
        login_register_button.setOnClickListener{

            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun Login(email: String, pw: String) {

        val database: UserDatabase = UserDatabase.getInstance(this)
        val userDao: UserDao=database.userDao
        val userList = ArrayList<User>()
        val loginThread = Thread { userList.addAll(userDao.userLogin(email,pw)) }
        //입력한 email,pw의 정보를 가진 게 있는지 쿼리를 날리고 있다면 userList에 저장
        loginThread.start()

        try {
            loginThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        if (userList.size == 0) {
            Toast.makeText(this, "일치하는 계정이 없습니다", Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            this.finish()
        }

    }

}
