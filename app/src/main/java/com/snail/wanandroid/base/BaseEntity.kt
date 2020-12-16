package com.snail.wanandroid.base

data class BaseEntity<T>(
    var errorCode: Int = 0,
    var errorMsg: String?,
    var recordset: T
) {

    override fun toString(): String {
        return "Result{" +
                "errorCode='" + errorCode + '\''.toString() +
                ", errorMsg='" + errorMsg + '\''.toString() +
                ", recordset=" + recordset +
                '}'.toString()
    }
}
