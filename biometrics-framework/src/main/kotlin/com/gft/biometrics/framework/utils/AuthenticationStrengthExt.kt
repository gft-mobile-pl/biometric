package com.gft.biometrics.framework.utils

import androidx.biometric.BiometricManager
import com.gft.biometrics.domain.model.AuthenticationStrength

fun AuthenticationStrength.toAuthenticator() = when (this) {
    AuthenticationStrength.WEAK -> BiometricManager.Authenticators.BIOMETRIC_WEAK
    AuthenticationStrength.STRONG -> BiometricManager.Authenticators.BIOMETRIC_STRONG
    AuthenticationStrength.DEVICE_CREDENTIAL -> BiometricManager.Authenticators.DEVICE_CREDENTIAL
}
