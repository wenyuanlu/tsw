/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qichuang.retrofit

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.qichuang.bean.CommonRetrofitBaseJsonResponse
import com.qichuang.commonlibs.utils.LogUtils
import okhttp3.ResponseBody
import retrofit2.Converter

internal class CommonJsonResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    @Throws(Exception::class)
    override fun convert(value: ResponseBody): T? {
        val responseBean: CommonRetrofitBaseJsonResponse = gson.fromJson(value.string(), CommonRetrofitBaseJsonResponse::class.java)

        //token校验
        try {
            if(responseBean.data is MutableMap<*, *>){
                val list :MutableMap<*,*> = responseBean.data as MutableMap<*, *>
                if(list.isNullOrEmpty()){
                    responseBean.data = null
                }
            }

            val json: String = gson.toJson(responseBean)
            LogUtils.LOGE(json)
            return adapter.fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
