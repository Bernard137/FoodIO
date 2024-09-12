package com.example.foodio.data

import com.example.foodio.models.User

object UserRepository {
    private val users: MutableList<User> = mutableListOf()

    fun add(user: User) = users.add(user)

    //fun clearAllUsers() = users.clear()
    fun fetchUser(username: String, password: String): User? {
        users.forEach {
            if (it.username == username && it.password != password) {
                return User("", "")
            } else if (it.username == username && it.password == password) {
                return it
            }
        }

        return null
    }
}