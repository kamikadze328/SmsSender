package com.kamikadze328.smssender

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kamikadze328.smssender.ui.MainScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(viewModel)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.showForegroundService) {
                        //startForegroundService()
                        viewModel.onEvent(MainUiEvent.ForegroundServiceStarted)
                    }
                    state.toastText?.let {
                        showToast(it)
                        viewModel.onEvent(MainUiEvent.OnToastShown)
                    }
                }
            }
        }
        viewModel.onEvent(MainUiEvent.OnInit(this))
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onEvent(
            MainUiEvent.OnPermissionsResult(
                requestCode = requestCode,
                grantResults = grantResults.toList(),
                activity = this
            )
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}

