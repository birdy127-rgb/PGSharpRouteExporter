package io.github.auag0.pgsharprouteexporter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.auag0.pgsharprouteexporter.R
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainAction

@Composable
fun FilePickerCard(
    modifier: Modifier = Modifier,
    onAction: (MainAction) -> Unit
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(text = stringResource(R.string.for_non_root_user))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.select_file_hint))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onAction(MainAction.SelectFile) }) {
                Text(text = stringResource(R.string.select_pgsdata_file))
            }
        }
    }
}