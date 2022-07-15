package com.example.jyghiretest.data.utils


interface Syncable {
    suspend fun sync(): Result<Unit>
}
