package com.example.jyghiretest.data.utils

interface Mapper<FROM, TO> {
    suspend operator fun invoke(from: FROM): TO
}
