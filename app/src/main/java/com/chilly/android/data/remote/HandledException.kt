package com.chilly.android.data.remote

import com.chilly.android.data.remote.dto.response.ErrorResponse

/*
    types of network exceptions:
    1. No internet or other causes that request cannot be send or response cannot be received like timeout
    2. Server exception - should be logged
    3. Deserialization exception - should be logged
    4. client exceptions - can be handled
*/

class HandledException(
    val errorResponse: ErrorResponse
) : Throwable(message = "${errorResponse.statusCode}: ${errorResponse.message}")