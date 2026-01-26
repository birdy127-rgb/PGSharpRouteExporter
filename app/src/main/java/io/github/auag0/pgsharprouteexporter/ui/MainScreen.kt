package io.github.auag0.pgsharprouteexporter.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainAction
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainUiState

@Composable
fun MainScreen(
    state: MainUiState,
    onAction: (MainAction) -> Unit
) {
    Scaffold { innerPadding ->
        MainContent(
            modifier = Modifier.padding(innerPadding),
            packageInfo = state.packageInfo,
            routes = state.routes,
            routeSource = state.routeSource,
            isLoading = state.isLoading,
            isRooted = state.isRooted,
            onAction = onAction
        )
    }
}