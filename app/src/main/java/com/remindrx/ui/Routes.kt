package com.remindrx.ui

sealed interface Route {
    val route: String
}

data object RouteDashboard : Route {
    override val route: String = "dashboard"
}

data object RouteHistory : Route {
    override val route: String = "history"
}

data object RouteSettings : Route {
    override val route: String = "settings"
}

data object RouteAddEditMedication : Route {
    override val route: String = "medication/edit?medicationId={medicationId}"
    fun create(medicationId: String? = null): String =
        if (medicationId == null) "medication/edit?medicationId=" else "medication/edit?medicationId=$medicationId"
}

data object RouteMedicationDetail : Route {
    override val route: String = "medication/detail/{medicationId}"
    fun create(medicationId: String): String = "medication/detail/$medicationId"
}

