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
        body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent accumsan laoreet imperdiet. Duis odio augue, mattis at accumsan eget, tincidunt vel sem. Vivamus consequat aliquam nisi id laoreet. Integer in enim a purus tristique eleifend eu non risus. Sed vestibulum dolor ut quam rutrum pellentesque. Sed tempor maximus mi. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla laoreet elit quis laoreet cursus.",
        userTags = listOf(
            fakeUserTagsList[0],
            fakeUserTagsList[3]
        )
    ),
    Note(
        id = 1,
        title = "Note 2",
        body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent accumsan laoreet imperdiet. Duis odio augue, mattis at accumsan eget, tincidunt vel sem. Vivamus consequat aliquam nisi id laoreet. Integer in enim a purus tristique eleifend eu non risus. Sed vestibulum dolor ut quam rutrum pellentesque.",
        userTags = listOf(
            fakeUserTagsList[3]
        )
    ),
    Note(
        id = 2,
        title = "Note 3",
        body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent accumsan laoreet imperdiet. Duis odio augue, mattis at accumsan eget, tincidunt vel sem. Vivamus consequat aliquam nisi id laoreet. Integer in enim a purus tristique eleifend eu non risus. Sed vestibulum dolor ut quam rutrum pellentesque. Sed tempor maximus mi.",
        userTags = listOf(
            fakeUserTagsList[1],
            fakeUserTagsList[2],
            fakeUserTagsList[3]
        )
    ),
    Note(
        id = 3,
        title = "Note 4",
        body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent accumsan laoreet imperdiet.",
        userTags = emptyList()
    ),
    Note(
        id = 4,
        title = "Note 5",
        body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent accumsan laoreet imperdiet. Duis odio augue, mattis at accumsan eget, tincidunt vel sem. Vivamus consequat aliquam nisi id laoreet.",
        userTags = listOf(
            fakeUserTagsList[0]
        )
    )
)