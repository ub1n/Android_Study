package kr.ac.smu.cs.study

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        private var instance: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UserDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "user.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as UserDatabase
        }
    }
}