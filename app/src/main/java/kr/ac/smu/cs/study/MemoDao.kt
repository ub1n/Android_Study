package kr.ac.smu.cs.study

import androidx.room.*

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo ") //email 값이 등록한 email과 같은 유저 전부 보이는 쿼리
    fun getMemo(): List<Memo>

    @Delete
    fun delete(memo:Memo)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(memo:Memo)

    @Insert(onConflict = OnConflictStrategy.REPLACE) //정보 넣기
    fun insert(memo: Memo)


}