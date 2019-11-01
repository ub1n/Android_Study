package kr.ac.smu.cs.study

import android.graphics.Bitmap
import androidx.room.*

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo ") //email 값이 등록한 email과 같은 유저 전부 보이는 쿼리
    fun getMemo(): MutableList<Memo>

    @Query("DELETE FROM memo WHERE id = (:id)")
    fun delete(id:Int)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(memo:Memo)

    @Insert(onConflict = OnConflictStrategy.REPLACE) //정보 넣기
    fun insert(memo: Memo)




}
