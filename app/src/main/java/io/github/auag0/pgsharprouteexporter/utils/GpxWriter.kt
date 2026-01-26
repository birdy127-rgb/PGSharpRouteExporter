package io.github.auag0.pgsharprouteexporter.utils

import io.github.auag0.pgsharprouteexporter.model.Route

object GpxWriter {
    fun write(route: Route): String = buildString {
        appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
        appendLine("""<gpx version="1.1" creator="PGSharp Route Exporter">""")
        route.points.forEach {
            appendLine("""  <wpt lat="${it.latitude}" lon="${it.longitude}" />""")
        }
        appendLine("</gpx>")
    }
}