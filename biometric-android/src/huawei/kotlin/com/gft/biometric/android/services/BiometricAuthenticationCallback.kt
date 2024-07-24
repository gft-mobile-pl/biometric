package com.gft.biometric.android.services

import androidx.biometric.BiometricPrompt
import com.gft.biometric.api.model.BiometricAuthenticationError

internal class BiometricAuthenticationCallback(
    private val onAuthenticationSuccessful: () -> Unit,
    private val onAuthenticationFailed: (Exception) -> Unit,
) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        onAuthenticationSuccessful()
    }

    override fun onAuthenticationFailed() {
        onAuthenticationFailed(BiometricAuthenticationError.UserNotAuthenticated)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        onAuthenticationFailed(
            when (errorCode) {
                BiometricPrompt.ERROR_HW_NOT_PRESENT,
                BiometricPrompt.ERROR_HW_UNAVAILABLE -> BiometricAuthenticationError.BiometricSensorUnavailable

                BiometricPrompt.ERROR_LOCKOUT -> BiometricAuthenticationError.BiometricSensorLockedTemporarily
                BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> BiometricAuthenticationError.BiometricSensorLockedPermanently
                BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                BiometricPrompt.ERROR_USER_CANCELED,
                BiometricPrompt.ERROR_CANCELED -> BiometricAuthenticationError.AuthenticationCancelledByUser

                BiometricPrompt.ERROR_TIMEOUT -> BiometricAuthenticationError.AuthenticationTimeout
                BiometricPrompt.ERROR_NO_BIOMETRICS -> BiometricAuthenticationError.BiometricNotEnrolled
                BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> BiometricAuthenticationError.DeviceCredentialsNotSet
                BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> BiometricAuthenticationError.UserNotAuthenticated
                else -> BiometricAuthenticationError.UnknownBiometricAuthenticationError
            }
        )
    }
}
