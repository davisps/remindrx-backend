package com.remindrx.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



private val Context.dataStore by preferencesDataStore(name = "remindrx_prefs")

class AuthStore(private val context: Context) {
    private val keyEmail = stringPreferencesKey("current_user_email")

    val currentUserEmail: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[keyEmail] }

    suspend fun setCurrentUser(email: String?) {
        context.dataStore.edit { prefs ->
            if (email == null) prefs.remove(keyEmail) else prefs[keyEmail] = email
        }
    }
}

