package com.gft.biometrics.framework.services

import androidx.biometric.BiometricManager
import com.gft.biometrics.domain.model.AuthenticationStrength
import com.gft.biometrics.domain.services.BiometricAuthenticationStatus
import com.gft.biometrics.framework.utils.toAuthenticator

class AndroidXBiometricAuthenticationStatus(
    private val biometricManager: BiometricManager
) : BiometricAuthenticationStatus {

    override fun isBiometricAuthenticationSupported(authenticationStrength: AuthenticationStrength) =
        getBiometricStatus(authenticationStrength)
            .takeIf { status -> status.isKnownStatus() }
            .let { status ->
                status !in arrayOf(
                    BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
                    BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
                )
            }

    override fun areBiometricCredentialsEnrolled(authenticationStrength: AuthenticationStrength) =
        getBiometricStatus(authenticationStrength)
            .takeIf { status -> status.isKnownStatus() }
            .let { status ->
                status !in arrayOf(
                    BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
                    BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
                )
            }

    override fun isBiometricAuthenticationUsable(authenticationStrength: AuthenticationStrength) =
        getBiometricStatus(authenticationStrength) == BiometricManager.BIOMETRIC_SUCCESS

    private fun getBiometricStatus(authenticationStrength: AuthenticationStrength) =
        biometricManager.canAuthenticate(authenticationStrength.toAuthenticator())

    private fun Int.isKnownStatus() = this in arrayOf(
        BiometricManager.BIOMETRIC_SUCCESS,
        BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
    )
}
