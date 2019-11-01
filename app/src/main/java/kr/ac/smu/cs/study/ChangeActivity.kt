package kr.ac.smu.cs.study

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_change.*
import java.io.ByteArrayOutputStream
import java.util.*

class ChangeActivity : AppCompatActivity() {
    var memo=Memo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        val title=intent.getStringExtra("title")
        val content=intent.getStringExtra("content")
        val year=intent.getIntExtra("year",0)
        val month=intent.getIntExtra("month",0)
        val day=intent.getIntExtra("day",0)
        val hour=intent.getIntExtra("hour",0)
        val minute=intent.getIntExtra("minute",0)
        val id=intent.getIntExtra("id",0)
        //val byteString=intent.getStringExtra("image")
        //val byteArray=Base64.getDecoder().decode(byteString)
        val byteArray=intent.getByteArrayExtra("image")
        if(byteArray!=null){
        val picture= BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        //memo.image=picture
        memo.image=byteArray
            c_imageView.setImageBitmap(picture)}
        memo.title=title
        memo.content=content
        memo.year=year
        memo.month=month
        memo.day=day
        memo.hour=hour
        memo.minute=minute
        memo.id=id


        val database: MemoDatabase = MemoDatabase.getInstance(applicationContext)
        val memoDao: MemoDao=database.memoDao
        c_datebutton.text="${memo.year}년 ${memo.month}월 ${memo.day}일"
        c_ContentText.setText(content)
        c_titleText.setText(title)
        c_timebutton.text="${memo.hour} : ${memo.minute}"
        c_addbutton.setOnClickListener { view->   //등록-> 메모클래스에 빈부분이 하나라도 있으면 안됨(id포함)
            memo.title=c_titleText.text.toString()
            memo.content=c_ContentText.text.toString()

            Thread { database.memoDao.update(memo) }.start()
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            this.finish()
        }
        c_delbutton.setOnClickListener { view->    //삭제 -> 메모클래스에 빈부분이 하나라도있으면 안됨(id포함)
            Thread { database.memoDao.delete(memo.id) }.start()
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            this.finish()
        }
        c_imageView.setOnClickListener{
            var intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,123)
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            memo.hour=h
            memo.minute=m
            c_timebutton.text = "${h} : ${m}"

        }),hour,minute,false)

        tpd.show()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            memo.day=dayOfMonth
            memo.month=monthOfYear+1
            memo.year=year
            c_datebutton.text="${memo.year}년"+" "+"${memo.month}월"+" "+"${memo.day}일"
        }, year, month, day)

        dpd.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            /*var bmp= data?.extras?.get("data") as Bitmap
            c_imageView.setImageBitmap(bmp)
            memo.image=bmp*/
            var bmp=data?.extras?.get("data") as Bitmap
            var stream= ByteArrayOutputStream()
            bmp?.compress(Bitmap.CompressFormat.JPEG,100,stream)
            var byteArray=stream.toByteArray()
           // var w=Base64.getEncoder().encodeToString(byteArray)
            memo.image=byteArray
            c_imageView.setImageBitmap(bmp)
        }
    }
}
