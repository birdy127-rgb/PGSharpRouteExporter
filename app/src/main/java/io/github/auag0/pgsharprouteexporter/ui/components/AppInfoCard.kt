package io.github.auag0.pgsharprouteexporter.ui.components

import android.content.pm.PackageInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.PackageInfoCompat
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import io.github.auag0.pgsharprouteexporter.R

@Composable
fun AppInfoCard(
    modifier: Modifier = Modifier,
    packageInfo: PackageInfo?
) {
    val context = LocalContext.current
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (packageInfo == null) {
                Text(
                    text = stringResource(id = R.string.not_installed),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.headlineSmall
                )
            } else {
                val appInfo = packageInfo.applicationInfo
                val pm = context.packageManager
                Row {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(appInfo?.loadIcon(pm) ?: android.R.drawable.sym_def_app_icon)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = appInfo?.loadLabel(pm).toString())
                        Text(
                            text = "${packageInfo.versionName} (${
                                PackageInfoCompat.getLongVersionCode(packageInfo)
                            })"
                        )
                    }
                }
            }
        }
    }
}