package com.example.model

data class UserTag(
    val id: Long = -1,
    val tagText: String = ""
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