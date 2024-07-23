package com.gft.biometrics

import android.app.Application
import android.hardware.biometrics.BiometricManager.Authenticators
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gft.biometrics.domain.model.AuthenticationStrength
import com.gft.biometrics.domain.services.BiometricAuthenticator
import com.gft.biometrics.framework.services.AndroidXBiometricAuthenticator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun BiometricsView(
    viewModel: BiometricsViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()


    Column(
        Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.authenticate(AuthenticationStrength.WEAK) },
            enabled = state != State.Pending
        ) {
            Text("Weak (f.e., Face)")
        }
        Button(
            onClick = { viewModel.authenticate(AuthenticationStrength.STRONG) },
            enabled = state != State.Pending
        ) {
            Text("Strong (f.e., Finger)")
        }
        Button(
            onClick = { viewModel.authenticate(AuthenticationStrength.DEVICE_CREDENTIAL) },
            enabled = state != State.Pending
        ) {
            Text("Device Credential (f.e., PIN")
        }
    }
}

internal sealed interface State {
    object Idle : State
    object Pending : State
    object Success : State
    data class Error(val error: Throwable) : State
}

internal class BiometricsViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    private val biometricsService = AndroidXBiometricAuthenticator(application)

    val state = MutableStateFlow<State>(State.Idle)

    fun authenticate(strength: AuthenticationStrength) = viewModelScope.launch {
        withState {
            biometricsService.authenticate(
                strength,
                BiometricAuthenticator.AuthenticationPromptParams(
                    title = "Testing auth strength: $strength",
                    subtitle = "Subtitle",
                    description = "Description",
                    negativeButtonLabel = "Cancel",
                )
            )
        }
    }

    private suspend fun withState(block: suspend () -> Unit) {
        state.update { State.Pending }
        runCatching { block() }
            .fold(
                onSuccess = { State.Success },
                onFailure = { err -> State.Error(err) }
            )
            .let { newState -> state.update{ newState } }
    }
}
