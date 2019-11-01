package kr.ac.smu.cs.study

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.ItemTouchHelper
//import sun.util.locale.provider.LocaleProviderAdapter.getAdapter
import androidx.recyclerview.widget.RecyclerView








class MainActivity : AppCompatActivity() {
    private var memoDatabase:MemoDatabase?=null
    private var memoList=mutableListOf<Memo>()

    lateinit var mAdapter:MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        regbutton.setOnClickListener{ view->   //임시 등록버튼
            var intent= Intent(this,AddActivity::class.java)
            startActivityForResult(intent,2)
        }
        memoDatabase=MemoDatabase.getInstance(this)

        mAdapter= MemoAdapter(memoList,applicationContext)
        val r = Runnable {   //recyclerview와 android room 결합
            try {
                memoList = memoDatabase?.memoDao?.getMemo()!!
                mAdapter = MemoAdapter(memoList, applicationContext)
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

        val database: MemoDatabase = MemoDatabase.getInstance(applicationContext)
        val memoDao: MemoDao=database.memoDao
        val memoList=ArrayList<Memo>()
        Thread{memoList.addAll(memoDao.getMemo())}.start()
        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //showToast("on Move")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // 삭제되는 아이템의 포지션을 가져온다
                val position = viewHolder.adapterPosition
                // 데이터의 해당 포지션을 삭제한다
                //showToast("on remove " + mList.remove(position))

                // 아답타에게 알린다
                Thread { database.memoDao.delete(memoList[position].id) }.start()

                val adapter = mRecyclerView.adapter as MemoAdapter
                adapter.remove(position)
                //mRecyclerView.adapter?.notifyItemRemoved(position)



                //memoList.removeAt(position)
                //var intent=Intent(applicationContext,MainActivity::class.java)
                // startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))


            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)

       /* var checkBox:CheckBox=findViewById(R.id.checkBox)
        delbutton.setOnClickListener { view->
            if(checkBox.isChecked)
                delbutton.text="선택됨"
        }*/

    }



}
