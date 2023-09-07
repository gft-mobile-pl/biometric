package com.gft.biometrics.framework.services

import androidx.biometric.BiometricManager
import com.gft.biometrics.domain.model.AuthenticationStrength
import com.gft.biometrics.domain.services.BiometricAuthenticationStatus
import com.gft.biometrics.framework.utils.toAuthenticator

class AndroidXBiometricAuthenticationStatus(
    private val biometricManager: BiometricManager
) : BiometricAuthenticationStatus {
    override fun isBiometricAuthenticationSupported(authenticationStrength: AuthenticationStrength) =
        isBiometricAuthenticationSupported(getBiometricStatus(authenticationStrength))

    private fun isBiometricAuthenticationSupported(biometricStatus: Int) =
        biometricStatus != BiometricManager.BIOMETRIC_STATUS_UNKNOWN &&
            biometricStatus != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE &&
            biometricStatus != BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED

    override fun areBiometricCredentialsEnrolled(authenticationStrength: AuthenticationStrength): Boolean {
        val biometricStatus = getBiometricStatus(authenticationStrength)
        return isBiometricAuthenticationSupported(biometricStatus) &&
            biometricStatus != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    }

    private fun getBiometricStatus(authenticationStrength: AuthenticationStrength) =
        biometricManager.canAuthenticate(authenticationStrength.toAuthenticator())
}
