package io.github.auag0.pgsharprouteexporter.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.auag0.pgsharprouteexporter.data.RouteRepository
import io.github.auag0.pgsharprouteexporter.model.Route
import io.github.auag0.pgsharprouteexporter.model.RouteSource
import io.github.auag0.pgsharprouteexporter.utils.RootUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val app: Application,
    private val routeRepository: RouteRepository
) : AndroidViewModel(app) {
    private var _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<ToastEvent>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        checkRoot()
    }

    fun requestSaveRoute(route: Route) {
        _uiState.update {
            if (it.pendingSaveRoute == null) {
                it.copy(pendingSaveRoute = route)
            } else it
        }
    }

    fun clearPendingSaveRoute() {
        _uiState.update { it.copy(pendingSaveRoute = null) }
    }

    private fun checkRoot() {
        val rooted = RootUtils.checkRoot()
        _uiState.update { it.copy(isRooted = rooted) }
    }

    fun loadFromRoot() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, routes = emptyList()) }

            val packageInfo = runCatching {
                app.packageManager.getPackageInfo("com.nianticlabs.pokemongo", 0)
            }.getOrNull() ?: return@launch

            _uiState.update { it.copy(packageInfo = packageInfo) }

            val routes = routeRepository.loadFromRoot(packageInfo.applicationInfo!!)
            updateRoutes(routes, RouteSource.ROOT)
        }
    }

    fun loadFromFile(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, routes = emptyList()) }

            val routes = routeRepository.loadFromFile(app, uri)
            updateRoutes(routes, RouteSource.FILE)
        }
    }

    fun saveRoute(uri: Uri) {
        val route = uiState.value.pendingSaveRoute ?: return
        viewModelScope.launch {
            routeRepository.saveAsGpx(app, route, uri)
            _toastEvent.emit(ToastEvent.SavedAsGpx)
            _uiState.update { it.copy(pendingSaveRoute = null) }
        }
    }

    private suspend fun updateRoutes(routes: List<Route>, source: RouteSource) {
        if (routes.isEmpty()) {
            _toastEvent.emit(ToastEvent.EmptyRoutes)
        }
        _uiState.update {
            it.copy(
                routes = routes,
                routeSource = source,
                isLoading = false
            )
        }
    }
}