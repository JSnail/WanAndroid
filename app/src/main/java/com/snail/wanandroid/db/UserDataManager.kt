package com.snail.wanandroid.db

import com.snail.wanandroid.db.dao.UserDao
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.utils.SharePreferencesUtils
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
class UserDataManager private constructor() :KoinComponent{

    private val dataBase: AppDataBase by inject ()
    private val sp = SharePreferencesUtils.instance
     var currentUserEntity: UserEntity? = null

    val isLogged: Boolean
        get() = currentUserEntity != null


    fun logout() {
        sp.cookie = ""
        currentUserEntity = null
    }

    suspend fun getCurrentUserData(): UserEntity? {
        if (null == currentUserEntity) {
            val userId = sp.tempUserId
            if (userId != -1) {
                currentUserEntity = dataBase.userDao().queryUserById(userId)
            }
        }
        return currentUserEntity
    }


    private object Handler {
        val handler = UserDataManager()
    }

    companion object {
        val instance = Handler.handler
    }
}