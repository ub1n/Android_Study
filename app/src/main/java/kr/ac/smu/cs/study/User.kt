package kr.ac.smu.cs.study

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(var email: String?, var pw: String?) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
