package com.remindrx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.remindrx.data.DoseLogEntity
import com.remindrx.data.MedicationEntity
import com.remindrx.ui.vm.HistoryViewModel
import com.remindrx.ui.vm.MedicationsViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MedicationDetailScreen(
    medicationId: String,
    medsVm: MedicationsViewModel,
    historyVm: HistoryViewModel,
    onEdit: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var med by remember { mutableStateOf<MedicationEntity?>(null) }
    val logs by historyVm.observeForMedication(medicationId).collectAsState(initial = emptyList())
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    LaunchedEffect(medicationId) {
        med = medsVm.loadMedication(medicationId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A2B4A))
                .padding(
                    top = statusBarPadding.calculateTopPadding() + 16.dp, 
                    bottom = 20.dp, 
                    start = 8.dp, 
                    end = 8.dp
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Medication Details",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                IconButton(onClick = onEdit, enabled = med != null) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }
            }
        }

        if (med == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading…")
            }
            return@Column
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(med!!.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "${med!!.dosage} • ${med!!.dailyTime}", 
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (med!!.specialInstructions.isNotBlank()) {
                Text(
                    text = med!!.specialInstructions,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            val refillSoon = med!!.remainingDoses <= med!!.refillThreshold
            Text(
                "Remaining doses: ${med!!.remainingDoses} (refill at ${med!!.refillThreshold})",
                style = MaterialTheme.typography.bodyMedium,
                color = if (refillSoon) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { medsVm.markTaken(context, medicationId) },
            ) { 
                Text("Mark dose as taken") 
            }

            Spacer(Modifier.height(16.dp))
            Text("Recent adherence", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (logs.isEmpty()) {
                    item {
                        Text(
                            "No logs yet. Use “Mark dose as taken” to create your first entry.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(logs.take(50), key = { it.logId }) { log ->
                        LogRow(log)
                    }
                }
            }
        }
    }
}

@Composable
private fun LogRow(log: DoseLogEntity) {
    val dt = Instant.ofEpochMilli(log.timestampEpochMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatted = remember(dt) {
        dt.format(DateTimeFormatter.ofPattern("MMM d, yyyy • HH:mm"))
    }
    Text(
        text = "$formatted • ${log.status}",
        style = MaterialTheme.typography.bodyMedium
    )
}
