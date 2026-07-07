package com.remindrx.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindrx.auth.AuthStore
import com.remindrx.auth.PasswordHasher
import com.remindrx.data.AppDatabase
import com.remindrx.data.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val db: AppDatabase,
    private val authStore: AuthStore,
) : ViewModel() {
    val currentEmail = authStore.currentUserEmail
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, null)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }

    fun register(email: String, name: String, password: String) {
        viewModelScope.launch {
            clearError()
            if (email.isBlank() || password.length < 6 || name.isBlank()) {
                _error.value = "Enter a name, email, and password (6+ chars)."
                return@launch
            }
            val existing = db.userDao().findByEmail(email)
            if (existing != null) {
                _error.value = "Account already exists for that email."
                return@launch
            }
            val hash = PasswordHasher.hashPassword(password)
            db.userDao().insert(
                UserEntity(
                    email = email,
                    name = name,
                    passwordSaltBase64 = hash.saltBase64,
                    passwordHashBase64 = hash.hashBase64,
                ),
            )
            authStore.setCurrentUser(email)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            clearError()
            if (email.isBlank() || password.isBlank()) {
                _error.value = "Enter your email and password."
                return@launch
            }
            val user = db.userDao().findByEmail(email)
            if (user == null) {
                _error.value = "No account found for that email."
                return@launch
            }
            val ok = PasswordHasher.verify(password, user.passwordSaltBase64, user.passwordHashBase64)
            if (!ok) {
                _error.value = "Incorrect password."
                return@launch
            }
            authStore.setCurrentUser(email)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authStore.setCurrentUser(null)
        }
    }
}

