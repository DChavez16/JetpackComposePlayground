package com.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Note(
    val id: Long = -1,
    val title: String = "",
    val body: String = "",
    @SerialName("user_tags") val userTags: List<UserTag> = emptyList()
)


val fakeNotesList = listOf(
    Note(
        id = 0,
        title = "Note 1",
        body = "",
        userTags = listOf(
            fakeUserTagsList[0],
            fakeUserTagsList[3]
        )
    ),
    Note(
        id = 1,
        title = "Note 2",
        body = "Note 2 body",
        userTags = listOf(
            fakeUserTagsList[3]
        )
    ),
    Note(
        id = 2,
        title = "Note 3",
        body = "Note 3 body",
        userTags = listOf(
            fakeUserTagsList[1],
            fakeUserTagsList[2],
            fakeUserTagsList[3]
        )
    ),
    Note(
        id = 3,
        title = "Note 4",
        body = "",
        userTags = emptyList()
    ),
    Note(
        id = 4,
        title = "Note 5",
        body = "Note 5 body",
        userTags = listOf(
            fakeUserTagsList[0]
        )
    )
)