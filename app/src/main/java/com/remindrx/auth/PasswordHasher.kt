package com.remindrx.auth

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

data class PasswordHash(
    val saltBase64: String,
    val hashBase64: String,
)

object PasswordHasher {
    private val rng = SecureRandom()

    fun hashPassword(password: String): PasswordHash {
        val salt = ByteArray(16).also { rng.nextBytes(it) }
        val hash = sha256(salt + password.toByteArray(Charsets.UTF_8))
        return PasswordHash(
            saltBase64 = Base64.getEncoder().encodeToString(salt),
            hashBase64 = Base64.getEncoder().encodeToString(hash),
        )
    }

    fun verify(password: String, saltBase64: String, hashBase64: String): Boolean {
        val salt = Base64.getDecoder().decode(saltBase64)
        val expected = Base64.getDecoder().decode(hashBase64)
        val actual = sha256(salt + password.toByteArray(Charsets.UTF_8))
        return MessageDigest.isEqual(expected, actual)
    }

    private fun sha256(bytes: ByteArray): ByteArray =
        MessageDigest.getInstance("SHA-256").digest(bytes)

    private operator fun ByteArray.plus(other: ByteArray): ByteArray {
        val out = ByteArray(this.size + other.size)
        System.arraycopy(this, 0, out, 0, this.size)
        System.arraycopy(other, 0, out, this.size, other.size)
        return out
    }
}

