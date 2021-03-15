package com.snail.wanandroid.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.utils.SharePreferencesUtils
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
class UserDataManager private constructor() : KoinComponent {

    private val dataBase: AppDataBase by inject()
    private val sp = SharePreferencesUtils.instance
    var currentUserEntity = MutableLiveData<UserEntity>()

    var isLogged: Boolean = false
        get() = currentUserEntity.value != null && sp.cookie.isNotEmpty()


    fun logout() {
        sp.cookie = ""
        sp.tempUserId =-1
        currentUserEntity.value = null
    }

    suspend fun getCurrentUserData() {
        if (null == currentUserEntity.value) {
            val userId = sp.tempUserId
            if (userId != -1) {
                val data = dataBase.userDao().queryUserById(userId)
                currentUserEntity.postValue(data)
                Log.i("TAG","data == ${data.nickname}")
            }else{
                currentUserEntity.postValue(null)
            }
        }
    }


    private object Handler {
        val handler = UserDataManager()
    }

    companion object {
        val instance = Handler.handler
    }
}