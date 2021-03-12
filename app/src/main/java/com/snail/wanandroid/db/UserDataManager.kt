package com.snail.wanandroid.db

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import com.snail.wanandroid.db.dao.UserDao
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.utils.SharePreferencesUtils
import kotlinx.coroutines.flow.filter
import org.koin.java.KoinJavaComponent.inject

class UserDataManager private constructor() {

    private val userDao: UserDao by inject(UserDao::class.java)
    private val sp =  SharePreferencesUtils.instance

    var isLogged = false
    var currentUserEntity = MutableLiveData<UserEntity>()
    set(value)  {
        if (null != value.value){
            isLogged = true
        }
        field = value
    }

    fun logout(){
        sp.cookie = ""
        currentUserEntity.value = null
        MediatorLiveData<UserEntity>().addSource(currentUserEntity){

        }
    }



    private object Handler {
        val handler = UserDataManager()
    }

    companion object {
        val instance = Handler.handler
    }
}