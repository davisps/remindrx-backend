package com.example.reminduirx

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reminduirx.ui.theme.RemindUIrxTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class PillReminder(
    val pillName: String,
    val time: String,
    val dosesPerDay: Int,
    val startDate: String,
    val endDate: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPillDialog(
    onDismiss: () -> Unit,
    onSubmit: (PillReminder) -> Unit
) {
    var pillName by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var doses by remember { mutableStateOf("") }
    val dateRangeState = rememberDateRangePickerState()
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val start = dateRangeState.selectedStartDateMillis?.let {
                    formatter.format(Date(it))
                } ?: "No start"
                val end = dateRangeState.selectedEndDateMillis?.let {
                    formatter.format(Date(it))
                } ?: "No end"

                onSubmit(
                    PillReminder(
                        pillName = pillName,
                        time = time,
                        dosesPerDay = doses.toIntOrNull() ?: 0,
                        startDate = start,
                        endDate = end
                    )
                )
                onDismiss()
            }) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                DateRangePicker(
                    state = dateRangeState,
                    modifier = Modifier.height(400.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pillName,
                    onValueChange = { pillName = it },
                    label = { Text("Pill Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g. 8:00 AM)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = doses,
                    onValueChange = { doses = it },
                    label = { Text("Doses per day") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}


@Composable
fun PillCard(pill: PillReminder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = pill.pillName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Time: ${pill.time}", fontSize = 16.sp)
            Text(text = "Doses per day: ${pill.dosesPerDay}", fontSize = 16.sp)
            Text(text = "From: ${pill.startDate} → ${pill.endDate}", fontSize = 16.sp)
        }
    }
}


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("History", Icons.Default.DateRange),
        NavItem("Profile", Icons.Default.Person),
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var pillList by remember { mutableStateOf(listOf<PillReminder>()) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            MedButton(onClick = { showDialog = true })
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(55.dp),
                windowInsets = WindowInsets(0)
            ) {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            pillList = pillList
        )
    }


    if (showDialog) {
        AddPillDialog(
            onDismiss = { showDialog = false },
            onSubmit = { newPill ->
                pillList = pillList + newPill
            }
        )
    }
}


@Composable
fun MedButton(onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.Add, "Add medication")
    }
}


@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    pillList: List<PillReminder> = listOf()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A2B4A))
                .padding(vertical = 20.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Medications",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            Text(
                text = "Welcome Back, User",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            pillList.forEach { pill ->
                PillCard(pill = pill)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    RemindUIrxTheme {
        MainScreen()
    }
}