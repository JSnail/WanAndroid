package com.snail.wanandroid.db.dao

import androidx.room.*
import com.snail.wanandroid.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull


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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

}