package io.github.auag0.pgsharprouteexporter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.auag0.pgsharprouteexporter.R
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainAction

@Composable
fun RootStatusCard(
    modifier: Modifier = Modifier,
    isRooted: Boolean,
    onAction: (MainAction) -> Unit
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.root_user))
            Spacer(modifier = Modifier.height(8.dp))
            if (isRooted) {
                Text(
                    text = stringResource(R.string.root_available),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = stringResource(R.string.root_not_available),
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onAction(MainAction.Reload) }) {
                Text(text = stringResource(R.string.reload))
            }
        }
    }
}