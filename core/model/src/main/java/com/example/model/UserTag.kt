package com.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserTag(
    val id: Long = -1,
    @SerialName("tag_text") val tagText: String = ""
)


val fakeUserTagsList = listOf(
    UserTag(
        id = 0,
        tagText = "Important"
    ),
    UserTag(
        id = 1,
        tagText = "Car"
    ),
    UserTag(
        id = 2,
        tagText = "School"
    ),
    UserTag(
        id = 3,
        tagText = "House"
    )
)