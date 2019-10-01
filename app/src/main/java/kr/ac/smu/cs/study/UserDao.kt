package kr.ac.smu.cs.study

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = (:email)")
    fun findUser(email: String): List<User>

    @Query("SELECT * FROM user WHERE email = (:email) AND pw = (:pw)")
    fun userLogin(email: String, pw: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}
