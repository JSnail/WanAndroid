package com.snail.wanandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.snail.wanandroid.db.dao.UserDao
import com.snail.wanandroid.entity.UserEntity


/**
 * @Author  Snail
 * @Date 1/3/21
 * @Description
 **/
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDataBase  : RoomDatabase() {

    abstract fun userDao(): UserDao


    companion object {
        private const val DATABASE_NAME = "SnailWanAndroid.db"

        @Volatile
        private var instance: AppDataBase? = null


        operator fun invoke(context: Context) {
            instance = buildNewDateBase(context)
        }

        private fun buildNewDateBase(context: Context) = Room.databaseBuilder(
            context, AppDataBase::class.java, DATABASE_NAME
        ).build()

        fun getInstance(context: Context): AppDataBase =
            instance ?: synchronized(this) {
                instance ?: buildNewDateBase(context).also {
                    instance = it
                }
            }


    }

}