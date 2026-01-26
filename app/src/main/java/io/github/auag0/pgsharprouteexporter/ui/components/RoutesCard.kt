package io.github.auag0.pgsharprouteexporter.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.github.auag0.pgsharprouteexporter.R
import io.github.auag0.pgsharprouteexporter.model.Route
import io.github.auag0.pgsharprouteexporter.model.RouteSource
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainAction

@Composable
fun RoutesCard(
    modifier: Modifier = Modifier,
    routes: List<Route>,
    routeSource: RouteSource,
    isLoading: Boolean,
    onAction: (MainAction) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    val text = when (routeSource) {
                        RouteSource.ROOT -> stringResource(R.string.routes_loaded_from_root)
                        RouteSource.FILE -> stringResource(R.string.routes_loaded_from_file)
                        RouteSource.NONE -> stringResource(R.string.no_routes_loaded)
                    }
                    Text(
                        text = text,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(routes) { route ->
                            RouteRow(
                                route = route,
                                onSave = { onAction(MainAction.SaveRoute(route)) }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}