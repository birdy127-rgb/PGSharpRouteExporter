package io.github.auag0.pgsharprouteexporter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.auag0.pgsharprouteexporter.R
import io.github.auag0.pgsharprouteexporter.model.Route

@Composable
fun RouteRow(route: Route, onSave: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_route),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = route.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${route.points.size} points",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        IconButton(onClick = onSave) {
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = stringResource(R.string.tooltip_save_as_gpx)
            )
        }
    }
}