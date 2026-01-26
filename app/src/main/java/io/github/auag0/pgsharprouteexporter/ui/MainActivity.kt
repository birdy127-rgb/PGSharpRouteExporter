package io.github.auag0.pgsharprouteexporter.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.auag0.pgsharprouteexporter.R
import io.github.auag0.pgsharprouteexporter.ui.theme.AppTheme
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainAction
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.MainViewModel
import io.github.auag0.pgsharprouteexporter.ui.viewmodel.ToastEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            lifecycleScope.launch {
                viewModel.loadFromFile(uri)
            }
        }
    }

    private val saveLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/gpx+xml")
    ) { uri ->
        if (uri != null) {
            viewModel.saveRoute(uri)
        } else {
            viewModel.clearPendingSaveRoute()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        viewModel.loadFromRoot()

        setContent {
            AppTheme {
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    viewModel.toastEvent.collect { event ->
                        val msg = when (event) {
                            ToastEvent.EmptyRoutes -> getString(R.string.toast_empty_routes)
                            ToastEvent.SavedAsGpx -> getString(R.string.toast_saved_as_gpx)
                            ToastEvent.FileLoadedSuccessfully -> getString(R.string.toast_file_loaded_successfully)
                            ToastEvent.NoRoutesInFile -> getString(R.string.toast_no_routes_in_file)
                            is ToastEvent.Error -> event.e.message
                                ?: getString(R.string.error_unknown)
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                }

                MainScreen(
                    state = uiState,
                    onAction = { action ->
                        when (action) {
                            MainAction.SelectFile -> {
                                filePickerLauncher.launch(arrayOf("*/*"))
                            }

                            is MainAction.SaveRoute -> {
                                viewModel.requestSaveRoute(action.route)
                                saveLauncher.launch("${action.route.name}.gpx")
                            }

                            MainAction.Reload -> {
                                viewModel.loadFromRoot()
                            }
                        }
                    }
                )
            }
        }
    }
}