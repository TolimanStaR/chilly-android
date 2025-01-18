package com.chilly.android.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post

suspend inline fun <reified T> HttpClient.getResult(
    url: String,
    block: HttpRequestBuilder.() -> Unit
): Result<T> = runCatching { get(url, block).body() }

suspend inline fun <reified T> HttpClient.postWithResult(
    url: String,
    block: HttpRequestBuilder.() -> Unit
): Result<T> = runCatching { post(url, block).body() }