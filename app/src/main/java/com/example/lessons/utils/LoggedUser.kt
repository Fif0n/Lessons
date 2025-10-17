package com.example.lessons.utils

object LoggedUser {
    private var id: String? = null
    private var name: String? = null
    private var surname: String? = null
    private var role: String? = null

    fun logIn(id: String, name: String, surname: String, role: String) {
        this.id = id
        this.name = name
        this.surname = surname
        this.role = role
    }

    fun logOut() {
        id = null
        name = null
        surname = null
        role = null
    }

    fun isLogged(): Boolean {
        return id != null
    }

    fun getUserData(): Map<String, String?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "surname" to surname,
            "role" to role
        )
    }

    fun updateNames(name: String, surname: String) {
        this.name = name
        this.surname = surname
    }

    fun getId(): String? {
        return id
    }
}
