package kr.ac.smu.cs.study

import android.graphics.Bitmap
import androidx.room.*

@Entity
class Memo() { //저장할 정보, primaryKey가 필요
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var title:String?=null
    var content:String?=null
    var year:Int?=null
    var month:Int?=null
    var day:Int?=null
    var hour:Int?=null
    var minute:Int?=null
    var image: ByteArray?=null
        //@Embedded



    //var image:ByteArray?=null

}