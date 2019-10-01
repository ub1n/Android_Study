package kr.ac.smu.cs.study

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(var email: String?, var pw: String?) { //저장할 정보, primaryKey가 필요
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
