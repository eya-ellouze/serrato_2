package com.serrato.service

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response


class BasicAuthInterceptor(val username: String, val password: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .header("Authorization", Credentials.basic(username, password))
            .build()
        return chain.proceed(request)
    }
}
