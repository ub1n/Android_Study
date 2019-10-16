package kr.ac.smu.cs.study

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_memo.view.*

class MemoAdapter(private var memoList : List<Memo>) : RecyclerView.Adapter<MemoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = memoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        memoList[position].let{ item ->
            with(holder) {
                textView.text = item.title
            }
        }
        holder.itemView.setOnClickListener { view->   //수정에 정보날림
            val intent = Intent(view.context, ChangeActivity::class.java)
            intent.putExtra("title",memoList[position].title)
            intent.putExtra("content",memoList[position].content)
            intent.putExtra("year",memoList[position].year)
            intent.putExtra("month",memoList[position].month)
            intent.putExtra("day",memoList[position].day)
            intent.putExtra("hour",memoList[position].hour)
            intent.putExtra("minute",memoList[position].minute)
            intent.putExtra("id",memoList[position].id)
            view.context.startActivity(intent)
        }



    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) { //BindViewHolder에서 사용할 변수지정
        val textView: TextView = view.item_memo_title  //view.item_memo_title을 textView란 이름으로 BindViewHolder에서 사용

    }
}