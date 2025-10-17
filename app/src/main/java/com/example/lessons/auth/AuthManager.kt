package com.example.lessons.auth

import android.content.Context
import android.util.Log
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.lessons.utils.LoggedUser
import java.util.TimeZone

class AuthManager(context: Context) {
    private var tokenManager = TokenManager(context)
    private val secret = com.example.lessons.BuildConfig.BACKEND_API_SECRET

    fun isLogged(): Boolean = tokenManager.getJwt() != null

    private fun getToken(): String? {
        return tokenManager.getJwt()
    }

    private fun getRoleFromToken(): String? {
        return try {
            val algorithm = Algorithm.HMAC256(secret)
            val verifier = JWT.require(algorithm).acceptLeeway(30).build()

            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

            val decodedJwt = verifier.verify(getToken())
            setUserData(
                decodedJwt.getClaim("id").asString(),
                decodedJwt.getClaim("name").asString(),
                decodedJwt.getClaim("surname").asString(),
                decodedJwt.getClaim("role").asString(),
            )
            decodedJwt.getClaim("role").asString()
        } catch (e: JWTDecodeException) {
            null
        } catch (e: TokenExpiredException) {
            null
        } catch (e: JWTVerificationException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun checkAccess(requiredRole: String?): Boolean {
        if (requiredRole == null) return true

        val role = getRoleFromToken() ?: return false
        return requiredRole == role
    }

    private fun setUserData(id: String, name: String, surname: String, role: String) {
        LoggedUser.logIn(id, name, surname, role)
    }
}