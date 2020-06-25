package com.vonage.tutorial.voice

data class User(
    val name: String,
    val jwt: String
)

object Config {

    val alice = User(
        "Alice",
        "" // TODO: "set Alice's JWT token"
    )
    val bob = User(
        "Bob",
        "" // TODO: "set Bob JWT token"
    )

    fun getOtherUserName(userName: String) = if (userName == alice.name) bob.name else alice.name
}