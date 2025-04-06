package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.TokenHolder
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpClient.getResult(
    url: String,
    block: HttpRequestBuilder.() -> Unit
): Result<T> = runCatching { get(url, block).body() }

suspend inline fun <reified T> HttpClient.postWithResult(
    url: String,
    block: HttpRequestBuilder.() -> Unit
): Result<T> = runCatching { post(url, block).body() }

suspend inline fun <reified T> HttpClient.postWithResult(
    url: String,
    onResponse: HttpResponse.() -> T,
    block: HttpRequestBuilder.() -> Unit
): Result<T> = runCatching {
    post(url, block).onResponse()
}

suspend inline fun HttpClient.wrappedPost(
    url: String,
    block: HttpRequestBuilder.() -> Unit
): Result<Unit> = runCatching { post(url, block) }

suspend inline fun HttpClient.wrappedPut(
    url: String,
    block: HttpRequestBuilder.() -> Unit
): Result<Unit> = runCatching { put(url, block) }

fun HttpRequestBuilder.setAuthorization(holder: TokenHolder) {
    holder.accessToken?.let {
        bearerAuth(it)
    }
}