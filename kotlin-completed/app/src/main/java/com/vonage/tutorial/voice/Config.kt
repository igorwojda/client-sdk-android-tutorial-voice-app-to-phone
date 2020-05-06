package com.vonage.tutorial.voice

data class User(
    val name: String,
    val jwt: String
)

object Config {

    const val CONVERSATION_ID: String = ""

    val user1 = User(
        "USER1",
        ""
    )
    val user2 = User(
        "USER2",
        ""
    )
}