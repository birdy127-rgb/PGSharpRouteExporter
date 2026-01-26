package io.github.auag0.pgsharprouteexporter.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.Uri
import io.github.auag0.pgsharprouteexporter.model.Route

interface RouteRepository {
    suspend fun loadFromRoot(appInfo: ApplicationInfo): List<Route>
    suspend fun loadFromFile(context: Context, uri: Uri): List<Route>
    suspend fun saveAsGpx(context: Context, route: Route, uri: Uri)
}