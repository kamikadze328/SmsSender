package com.kamikadze328.smssender

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kamikadze328.smssender.ui.SmsListScreenUi
import com.kamikadze328.smssender.ui.theme.SmsSenderTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class SmsListActivity : ComponentActivity(), KoinComponent {
    private val viewModel: SmsListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SmsSenderTheme {
                SmsListScreenUi(viewModel)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.toastText?.let {
                        showToast(it)
                        viewModel.onEvent(SmsListUiEvent.OnToastShown)
                    }
                }
            }
        }
        viewModel.onEvent(SmsListUiEvent.OnInit(this))
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onEvent(
            SmsListUiEvent.OnPermissionsResult(
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

