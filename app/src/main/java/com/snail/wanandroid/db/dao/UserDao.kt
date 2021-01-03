package com.snail.wanandroid.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.snail.wanandroid.entity.UserEntity
import kotlinx.coroutines.flow.Flow


/**
 * @Author  Snail
 * @Date 1/3/21
 * @Description
 **/
@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity WHERE  id =:userId")
    fun queryUserById(userId: Int): Flow<UserEntity>

    @Query("SELECT *  FROM  UserEntity")
    suspend fun queryAllUser(): Array<UserEntity>

    @Insert
    suspend fun insertUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

}