package com.example.reminduirx

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reminduirx.ui.theme.RemindUIrxTheme

data class CompletedPill(
    val pillName: String,
    val time: String,
    val dosesPerDay: Int,
    val startDate: String,
    val endDate: String,
    val completedDate: String
)

@Composable
fun HistoryCard(pill: CompletedPill) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6F4EA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF34A853),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = pill.pillName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Taken at: ${pill.time}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Doses per day: ${pill.dosesPerDay}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "${pill.startDate} → ${pill.endDate}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFE6F4EA))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Completed ${pill.completedDate}",
                        fontSize = 12.sp,
                        color = Color(0xFF34A853),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun AdherenceHistoryScreen(
    completedPills: List<CompletedPill> = listOf()
) {
    // Same nav items as the dashboard screen
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("History", Icons.Default.DateRange),
        NavItem("Profile", Icons.Default.Person),
    )

    var selectedIndex by remember { mutableIntStateOf(1) }

    Scaffold(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A2B4A))
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Adherence History",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Medications you have completed",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Summary bar
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Total Completed", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${completedPills.size}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF34A853)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = Color.LightGray, thickness = 1.dp)

                Spacer(modifier = Modifier.height(8.dp))


                if (completedPills.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No completed medications yet",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        completedPills.forEach { pill ->
                            HistoryCard(pill = pill)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdherenceHistoryPreview() {
    RemindUIrxTheme {
        AdherenceHistoryScreen(
            completedPills = listOf(
                CompletedPill(
                    pillName = "Ibuprofen",
                    time = "8:00 AM",
                    dosesPerDay = 2,
                    startDate = "03/01/2025",
                    endDate = "03/10/2025",
                    completedDate = "03/10/2025"
                ),
                CompletedPill(
                    pillName = "Amoxicillin",
                    time = "12:00 PM",
                    dosesPerDay = 3,
                    startDate = "02/15/2025",
                    endDate = "02/25/2025",
                    completedDate = "02/25/2025"
                ),
                CompletedPill(
                    pillName = "Vitamin D",
                    time = "9:00 AM",
                    dosesPerDay = 1,
                    startDate = "01/01/2025",
                    endDate = "01/31/2025",
                    completedDate = "01/31/2025"
                )
            )
        )
    }
}