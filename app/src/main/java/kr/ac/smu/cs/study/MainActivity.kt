package kr.ac.smu.cs.study

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var memoDatabase:MemoDatabase?=null
    private var memoList=listOf<Memo>()

    lateinit var mAdapter:MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        regbutton.setOnClickListener{ view->   //임시 등록버튼
            var intent= Intent(this,AddActivity::class.java)
            startActivityForResult(intent,2)
        }
        memoDatabase=MemoDatabase.getInstance(this)

        mAdapter= MemoAdapter(memoList)
        val r = Runnable {   //recyclerview와 android room 결합
            try {
                memoList = memoDatabase?.memoDao?.getMemo()!!
                mAdapter = MemoAdapter(memoList)
                mAdapter.notifyDataSetChanged()

                mRecyclerView.adapter = mAdapter
                mRecyclerView.layoutManager = LinearLayoutManager(this)
                mRecyclerView.setHasFixedSize(true)
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()
       /* var checkBox:CheckBox=findViewById(R.id.checkBox)
        delbutton.setOnClickListener { view->
            if(checkBox.isChecked)
                delbutton.text="선택됨"
        }*/

    }
}
