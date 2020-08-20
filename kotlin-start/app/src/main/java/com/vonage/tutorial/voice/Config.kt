package com.vonage.tutorial.voice

data class User(
    val name: String,
    val jwt: String
)

object Config {

    val alice = User(
        "Alice",
        "ALICE_TOKEN" // TODO: "set Alice's JWT token"
    )
}