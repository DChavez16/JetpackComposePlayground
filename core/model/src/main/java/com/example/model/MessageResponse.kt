package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class MessageResponse(
    val message: String
)
