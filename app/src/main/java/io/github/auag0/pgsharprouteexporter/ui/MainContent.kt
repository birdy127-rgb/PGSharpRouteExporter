package io.github.auag0.pgsharprouteexporter.ui

import android.content.pm.PackageInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.auag0.pgsharprouteexporter.model.Route
import io.github.auag0.pgsharprouteexporter.model.RouteSource
import io.github.auag0.pgsharprouteexporter.ui.components.AppInfoCard
import io.github.auag0.pgsharprouteexporter.ui.components.FilePickerCard
import io.github.auag0.pgsharprouteexporter.ui.components.RootStatusCard
import io.github.auag0.pgsharprouteexporter.ui.components.RoutesCard
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainAction

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    packageInfo: PackageInfo?,
    routes: List<Route>,
    routeSource: RouteSource,
    isLoading: Boolean,
    isRooted: Boolean,
    onAction: (MainAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppInfoCard(packageInfo = packageInfo)

        Spacer(modifier = Modifier.height(8.dp))

        RoutesCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            routes = routes,
            routeSource = routeSource,
            isLoading = isLoading,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(8.dp))

        RootStatusCard(
            modifier = Modifier.fillMaxWidth(),
            isRooted = isRooted,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(8.dp))

        FilePickerCard(
            modifier = Modifier.fillMaxWidth(),
            onAction = onAction
        )
    }
}