package kr.ac.smu.cs.study

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = (:email)") //email 값이 등록한 email과 같은 유저 전부 보이는 쿼리
    fun findUser(email: String): List<User>

    @Query("SELECT * FROM user WHERE email = (:email) AND pw = (:pw)") //email 값과 pw와 같은 유저 전부 보이는 쿼리
    fun userLogin(email: String, pw: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //정보 넣기
    fun insert(user: User)
}
