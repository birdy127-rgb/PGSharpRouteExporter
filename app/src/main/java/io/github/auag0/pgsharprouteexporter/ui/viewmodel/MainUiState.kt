package io.github.auag0.pgsharprouteexporter.ui.viewmodel

import android.content.pm.PackageInfo
import io.github.auag0.pgsharprouteexporter.model.Route
import io.github.auag0.pgsharprouteexporter.model.RouteSource

data class MainUiState(
    val packageInfo: PackageInfo? = null,
    val routes: List<Route> = emptyList(),
    val isLoading: Boolean = false,
    val isRooted: Boolean = false,
    val routeSource: RouteSource = RouteSource.NONE,
    val pendingSaveRoute: Route? = null
)

sealed interface ToastEvent {
    data object EmptyRoutes : ToastEvent
    data object SavedAsGpx : ToastEvent
    data object FileLoadedSuccessfully : ToastEvent
    data object NoRoutesInFile : ToastEvent
    data class Error(val e: Throwable) : ToastEvent
}

sealed interface MainAction {
    data object SelectFile : MainAction
    data class SaveRoute(val route: Route) : MainAction
    data object Reload : MainAction
}