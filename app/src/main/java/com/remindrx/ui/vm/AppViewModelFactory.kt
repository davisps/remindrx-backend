package com.remindrx.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remindrx.auth.AuthStore
import com.remindrx.data.AppDatabase

class AppViewModelFactory(
    private val db: AppDatabase,
    private val authStore: AuthStore,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(db, authStore) as T
            modelClass.isAssignableFrom(MedicationsViewModel::class.java) ->
                MedicationsViewModel(db) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->
                HistoryViewModel(db) as T
            else -> error("Unknown ViewModel: ${modelClass.name}")
        }
    }
}

