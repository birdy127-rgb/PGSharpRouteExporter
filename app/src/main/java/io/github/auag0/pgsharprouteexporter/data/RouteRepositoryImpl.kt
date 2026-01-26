package io.github.auag0.pgsharprouteexporter.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.Uri
import com.topjohnwu.superuser.io.SuFile
import io.github.auag0.pgsharprouteexporter.model.LatLng
import io.github.auag0.pgsharprouteexporter.model.Route
import io.github.auag0.pgsharprouteexporter.utils.GpxWriter
import io.github.auag0.pgsharprouteexporter.utils.RootUtils.checkRoot
import io.github.auag0.pgsharprouteexporter.utils.SPParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStream
import java.io.ObjectInputStream
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor() : RouteRepository {
    private val prefsPaths = listOf(
        "shared_prefs/com.google.android.gms.safetynet.xml",
        "shared_prefs/com.google.android.gms.chimera.hl2.xml"
    )

    override suspend fun loadFromRoot(appInfo: ApplicationInfo): List<Route> =
        withContext(Dispatchers.IO) {
            if (!checkRoot()) return@withContext emptyList()

            val prefsFile = prefsPaths
                .map { SuFile(appInfo.dataDir, it) }
                .firstOrNull { it.exists() }
                ?: return@withContext emptyList()

            val xmlText = prefsFile.newInputStream().reader().use { it.readText() }
            val prefs = SPParser.parseXmlText(xmlText)
            val json = prefs["hlfavorRoute"]?.toString() ?: return@withContext emptyList()

            parseRoutes(json)
        }

    override suspend fun loadFromFile(context: Context, uri: Uri): List<Route> =
        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use {
                extractFromStream(it)?.let { json -> parseRoutes(json) }
            } ?: emptyList()
        }

    override suspend fun saveAsGpx(context: Context, route: Route, uri: Uri) {
        withContext(Dispatchers.IO) {
            val gpx = GpxWriter.write(route)
            context.contentResolver.openOutputStream(uri, "w")?.writer()?.use {
                it.write(gpx)
            }
        }
    }

    private fun extractFromStream(input: InputStream): String? =
        runCatching {
            ObjectInputStream(input).use {
                val map = it.readObject() as? HashMap<*, *>
                map?.get("hlfavorRoute") as? String
            }
        }.getOrNull()

    private fun parseRoutes(json: String): List<Route> {
        val array = JSONArray(json)
        return List(array.length()) { i ->
            val obj = array.getJSONObject(i)
            val pointsArray = obj.getJSONArray("points")
            val points = List(pointsArray.length()) { p ->
                val point = pointsArray.getJSONArray(p)
                LatLng(point.getDouble(0), point.getDouble(1))
            }
            Route(obj.getString("name"), points)
        }
    }
}