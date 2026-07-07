package com.remindrx.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.remindrx.ui.vm.AppViewModelFactory
import com.remindrx.ui.vm.HistoryViewModel
import com.remindrx.ui.vm.MedicationsViewModel

@Composable
fun MainScaffold(
    userEmail: String,
    factory: AppViewModelFactory,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    val backStack = navController.currentBackStackEntryAsState().value
    val currentRoute = backStack?.destination?.route

    val items = listOf(
        RouteDashboard to Icons.Default.Home,
        RouteHistory to Icons.Default.History,
        RouteSettings to Icons.Default.Settings,
    )

    Scaffold(
        bottomBar = {
            // Hide bottom bar on deep routes.
            val showBottom = currentRoute in setOf(RouteDashboard.route, RouteHistory.route, RouteSettings.route)
            if (showBottom) {
                NavigationBar {
                    items.forEach { (route, icon) ->
                        val selected = currentRoute == route.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(icon, contentDescription = null) },
                            label = null,
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == RouteDashboard.route) {
                FloatingActionButton(onClick = {
                    navController.navigate(RouteAddEditMedication.create())
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add medication")
                }
            }
        }
    ) { innerPadding ->
        val medsVm: MedicationsViewModel = viewModel(factory = factory)
        val historyVm: HistoryViewModel = viewModel(factory = factory)

        NavHost(
            navController = navController,
            startDestination = RouteDashboard.route,
            modifier = Modifier,
        ) {
            composable(RouteDashboard.route) {
                DashboardScreen(
                    userEmail = userEmail,
                    medsVm = medsVm,
                    onAdd = { navController.navigate(RouteAddEditMedication.create()) },
                    onOpen = { navController.navigate(RouteMedicationDetail.create(it)) },
                    contentPadding = innerPadding,
                )
            }
            composable(RouteHistory.route) {
                HistoryScreen(
                    userEmail = userEmail,
                    historyVm = historyVm,
                    contentPadding = innerPadding,
                )
            }
            composable(RouteSettings.route) {
                SettingsScreen(
                    userEmail = userEmail,
                    onLogout = onLogout,
                    contentPadding = innerPadding,
                )
            }

            composable(
                route = RouteAddEditMedication.route,
                arguments = listOf(navArgument("medicationId") { type = NavType.StringType; defaultValue = "" }),
            ) { entry ->
                val id = entry.arguments?.getString("medicationId")?.takeIf { it.isNotBlank() }
                AddEditMedicationScreen(
                    userEmail = userEmail,
                    medicationId = id,
                    medsVm = medsVm,
                    onDone = { navController.popBackStack() },
                )
            }

            composable(
                route = RouteMedicationDetail.route,
                arguments = listOf(navArgument("medicationId") { type = NavType.StringType }),
            ) { entry ->
                val medicationId = entry.arguments?.getString("medicationId")!!
                MedicationDetailScreen(
                    medicationId = medicationId,
                    medsVm = medsVm,
                    historyVm = historyVm,
                    onEdit = { navController.navigate(RouteAddEditMedication.create(medicationId)) },
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
