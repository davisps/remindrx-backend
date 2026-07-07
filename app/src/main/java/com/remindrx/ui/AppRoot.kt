package com.remindrx.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindrx.auth.AuthStore
import com.remindrx.data.AppDatabase
import com.remindrx.ui.vm.AppViewModelFactory
import com.remindrx.ui.vm.AuthViewModel

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val db = AppDatabase.get(context)
    val authStore = AuthStore(context)

    val factory = AppViewModelFactory(db, authStore)
    val authVm: AuthViewModel = viewModel(factory = factory)

    val currentEmail by authVm.currentEmail.collectAsState()
    if (currentEmail == null) {
        AuthScreen(authVm = authVm)
    } else {
        MainScaffold(
            userEmail = currentEmail!!,
            factory = factory,
            onLogout = { authVm.logout() },
        )
    }
}

