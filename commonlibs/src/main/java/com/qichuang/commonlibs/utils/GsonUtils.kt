package com.qichuang.commonlibs.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

object GsonUtils {

    fun fetchGson(): Gson {
        return Gson().newBuilder().disableHtmlEscaping().serializeNulls().create()
    }

    fun <T> toJson(data: T): String {
        return fetchGson().toJson(data)
    }

    fun fetchGsonObject(value: String?): JsonObject? {
        if (TextUtils.isEmpty(value)) {
            return null
        }

        return fetchGson().fromJson(value, JsonObject::class.java)
    }

    fun <T> GsonToMaps(gsonString: String?): Map<String?, T>? {
        return fetchGson().fromJson(gsonString, object : TypeToken<Map<String?, T>?>() {}.type)
    }

}