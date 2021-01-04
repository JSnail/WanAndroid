package com.snail.wanandroid.base

import com.squareup.moshi.Json

data class BaseEntity<T>(
    var errorCode: Int = 0,
    var errorMsg: String?,
    @Json(name = "data")
    var recordset: T?
) {

    override fun toString(): String {
        return "Result{" +
                "errorCode='" + errorCode + '\''.toString() +
                ", errorMsg='" + errorMsg + '\''.toString() +
                ", recordset=" + recordset +
                '}'.toString()
    }
}
