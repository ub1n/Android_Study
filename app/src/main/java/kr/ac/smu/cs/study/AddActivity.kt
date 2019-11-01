package kr.ac.smu.cs.study

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_add.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AddActivity : AppCompatActivity() {
    var memo=Memo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        datebutton.text="${year}년 ${month+1}월 ${day}일"
        val hour=c.get(Calendar.HOUR)
        val min=c.get(Calendar.MINUTE)
        timebutton.text="${hour} : ${min}"

        //디폴트로 현재시간 일단 넣어둠
        memo.year=year
        memo.month=month+1
        memo.day=day
        memo.hour=hour
        memo.minute=min
        addbutton.setOnClickListener { view->  //등록하기
            memo.title=titleText.text.toString()
            memo.content=ContentText.text.toString()
            val database: MemoDatabase = MemoDatabase.getInstance(applicationContext) //application context
            val memoDao: MemoDao=database.memoDao

            Thread { database.memoDao.insert(memo) }.start()
            doWorkWithConstraints()

            val intent = Intent(this, MainActivity::class.java) //로그인 액티비티로 돌아가기
            this.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            this.finish()
        }
        imageView.setOnClickListener{ //카메라부르기
            var intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,123)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            /*var bmp= data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(bmp)
            memo.image=bmp
            imageView.setImageBitmap(memo.image)*/

            //imageView.setImageBitmap(bmp)

            //bitmap-> byteArray->String
            //이런거 추가하면 삭제했다 다시깔아야댐 이거때문에 삽질 미쳤다
            var bmp=data?.extras?.get("data") as Bitmap
            var stream= ByteArrayOutputStream()
            bmp?.compress(Bitmap.CompressFormat.JPEG,100,stream)
            var byteArray=stream.toByteArray()
            //var w=Base64.getEncoder().encodeToString(byteArray)
            memo.image=byteArray
            imageView.setImageBitmap(bmp)
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(view: View) {  //timepicker 눌렀을때 선택창 띄워주고 정보 받음
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            memo.hour=h
            memo.minute=m
            timebutton.text = "${h} : ${m}"

        }),hour,minute,false)

        tpd.show()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickDataPicker(view: View) {  //datepicker 눌렀을때 선택창 띄워주고 정보받음
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            memo.day=dayOfMonth
            memo.month=monthOfYear+1
            memo.year=year
            datebutton.text="${memo.year}년"+" "+"${memo.month}월"+" "+"${memo.day}일"  //선택한 정보로 버튼수정
        }, year, month, day)

        dpd.show()
    }
    private fun getDelayTime(): Long {
        val c = Calendar.getInstance()

        val hour=c.get(Calendar.HOUR)
        val min=c.get(Calendar.MINUTE)
        val sec=c.get(Calendar.SECOND)


        val dataFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

        val todayDate = Date(System.currentTimeMillis())

        val currentTime = dataFormat.format(todayDate)



        val d1 = dataFormat.parse("${memo.hour}:${memo.minute}:00")

        val d2 = dataFormat.parse("$hour:$min:$sec")

        val diff = d1.time - d2.time



        return diff*1000



    }
    fun doWorkWithConstraints(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()
        val a="a"
        // 제약 조건과 함께 작업 요청 생성
        val requestConstraint  = OneTimeWorkRequestBuilder<AlarmWorker>().setInitialDelay(getDelayTime(),
            TimeUnit.MICROSECONDS)
            .build()

        val workManager = WorkManager.getInstance()

        workManager?.enqueue(requestConstraint)
    }
}
