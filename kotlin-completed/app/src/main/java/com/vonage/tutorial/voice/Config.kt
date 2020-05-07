package com.vonage.tutorial.voice

data class User(
    val name: String,
    val jwt: String
)

object Config {

    val alice = User(
        "Alice",
        ""
    )
    val bob = User(
        "Bob",
        ""
    )

    fun getOtherUserName(userName: String) = if (userName == alice.name) bob.name else alice.name
}