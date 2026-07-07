package com.remindrx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    userEmail: String,
    onLogout: () -> Unit,
    contentPadding: PaddingValues,
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = contentPadding.calculateBottomPadding())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A2B4A))
                .padding(
                    top = statusBarPadding.calculateTopPadding() + 20.dp,
                    bottom = 20.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Settings",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Signed in as", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(userEmail, style = MaterialTheme.typography.titleMedium)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                modifier = Modifier.fillMaxWidth(), 
                onClick = onLogout
            ) {
                Text("Log out")
            }
        }
    }
}
