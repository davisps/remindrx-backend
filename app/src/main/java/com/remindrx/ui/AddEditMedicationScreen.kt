package com.remindrx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.remindrx.data.MedicationEntity
import com.remindrx.ui.vm.MedicationsViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditMedicationScreen(
    userEmail: String,
    medicationId: String?,
    medsVm: MedicationsViewModel,
    onDone: () -> Unit,
) {
    val context = LocalContext.current
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    var isLoading by remember { mutableStateOf(medicationId != null) }
    var loaded by remember { mutableStateOf<MedicationEntity?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var dailyTime by remember { mutableStateOf("09:00") }
    var special by remember { mutableStateOf("") }
    var remaining by remember { mutableStateOf("30") }
    var threshold by remember { mutableStateOf("5") }
    var isActive by remember { mutableStateOf(true) }

    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(medicationId) {
        if (medicationId == null) return@LaunchedEffect
        val med = medsVm.loadMedication(medicationId)
        loaded = med
        if (med != null) {
            name = med.name
            dosage = med.dosage
            dailyTime = med.dailyTime
            special = med.specialInstructions
            remaining = med.remainingDoses.toString()
            threshold = med.refillThreshold.toString()
            isActive = med.isActive
        }
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A2B4A))
                .padding(
                    top = statusBarPadding.calculateTopPadding() + 16.dp, 
                    bottom = 20.dp, 
                    start = 8.dp, 
                    end = 16.dp
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDone) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = if (medicationId == null) "Add Medication" else "Edit Medication",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("Medication name") },
                singleLine = true,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text("Dosage (e.g., 10mg)") },
                singleLine = true,
            )

            // Time Picker Trigger
            val displayTime = remember(dailyTime) {
                runCatching {
                    LocalTime.parse(dailyTime).format(DateTimeFormatter.ofPattern("hh:mm a"))
                }.getOrDefault(dailyTime)
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true },
                value = displayTime,
                onValueChange = { },
                label = { Text("Daily reminder time") },
                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(Icons.Default.AccessTime, contentDescription = "Select time")
                },
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = special,
                onValueChange = { special = it },
                label = { Text("Special instructions (optional)") },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Active reminders", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = isActive, onCheckedChange = { isActive = it })
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = remaining,
                    onValueChange = { remaining = it },
                    label = { Text("Remaining doses") },
                    singleLine = true,
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = threshold,
                    onValueChange = { threshold = it },
                    label = { Text("Refill threshold") },
                    singleLine = true,
                )
            }

            if (!error.isNullOrBlank()) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                onClick = {
                    error = null
                    val rem = remaining.toIntOrNull()
                    val thr = threshold.toIntOrNull()
                    if (name.isBlank() || dosage.isBlank()) {
                        error = "Name and dosage are required."
                        return@Button
                    }
                    if (rem == null || rem < 0 || thr == null || thr < 0) {
                        error = "Remaining doses and threshold must be 0+."
                        return@Button
                    }
                    val id = loaded?.medicationId ?: UUID.randomUUID().toString()
                    val med = MedicationEntity(
                        medicationId = id,
                        userEmail = userEmail,
                        name = name.trim(),
                        dosage = dosage.trim(),
                        dailyTime = dailyTime.trim(),
                        specialInstructions = special.trim(),
                        remainingDoses = rem,
                        refillThreshold = thr,
                        isActive = isActive,
                    )
                    medsVm.upsertMedication(context, med)
                    onDone()
                },
            ) {
                Text("Save")
            }

            if (medicationId != null && loaded != null) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showDeleteConfirm = true },
                ) {
                    Text("Delete medication", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    if (showTimePicker) {
        val initialTime = runCatching { LocalTime.parse(dailyTime) }.getOrDefault(LocalTime.of(9, 0))
        var selectedHour by remember { mutableIntStateOf(if (initialTime.hour % 12 == 0) 12 else initialTime.hour % 12) }
        var selectedMinute by remember { mutableIntStateOf(initialTime.minute) }
        var selectedAmPm by remember { mutableStateOf(if (initialTime.hour < 12) "AM" else "PM") }

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val hour24 = when {
                        selectedAmPm == "AM" && selectedHour == 12 -> 0
                        selectedAmPm == "AM" -> selectedHour
                        selectedAmPm == "PM" && selectedHour == 12 -> 12
                        else -> selectedHour + 12
                    }
                    dailyTime = String.format("%02d:%02d", hour24, selectedMinute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            title = { Text("Select Time") },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    WheelPicker(
                        items = (1..12).toList(),
                        initialIndex = selectedHour - 1,
                        onItemSelected = { selectedHour = it }
                    )
                    Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                    WheelPicker(
                        items = (0..59).toList(),
                        initialIndex = selectedMinute,
                        format = { String.format("%02d", it) },
                        onItemSelected = { selectedMinute = it }
                    )
                    Spacer(Modifier.width(16.dp))
                    WheelPicker(
                        items = listOf("AM", "PM"),
                        initialIndex = if (selectedAmPm == "AM") 0 else 1,
                        onItemSelected = { selectedAmPm = it }
                    )
                }
            }
        )
    }

    if (showDeleteConfirm && loaded != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete medication?") },
            text = { Text("This will remove the medication and its reminders.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        medsVm.deleteMedication(context, loaded!!)
                        showDeleteConfirm = false
                        onDone()
                    },
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
fun <T> WheelPicker(
    items: List<T>,
    initialIndex: Int,
    format: (T) -> String = { it.toString() },
    onItemSelected: (T) -> Unit
) {
    val itemHeight = 40.dp
    val visibleItemsCount = 3
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collectLatest { index ->
                if (index < items.size) {
                    onItemSelected(items[index])
                }
            }
    }

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        // Selection highlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(Color.Gray.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Padding items at start
            item { Spacer(modifier = Modifier.height(itemHeight)) }
            
            items(items.size) { index ->
                val isSelected by remember {
                    derivedStateOf { listState.firstVisibleItemIndex == index }
                }
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = format(items[index]),
                        fontSize = if (isSelected) 20.sp else 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.alpha(if (isSelected) 1f else 0.5f)
                    )
                }
            }

            // Padding items at end
            item { Spacer(modifier = Modifier.height(itemHeight)) }
        }
    }
}
