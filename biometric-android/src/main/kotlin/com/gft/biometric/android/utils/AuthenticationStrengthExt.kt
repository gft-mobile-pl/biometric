package com.gft.biometric.android.utils

import androidx.biometric.BiometricManager
import com.gft.biometric.api.model.AuthenticationStrength

fun AuthenticationStrength.toAuthenticator() = when (this) {
    AuthenticationStrength.WEAK -> BiometricManager.Authenticators.BIOMETRIC_WEAK
    AuthenticationStrength.STRONG -> BiometricManager.Authenticators.BIOMETRIC_STRONG
    AuthenticationStrength.DEVICE_CREDENTIAL -> BiometricManager.Authenticators.DEVICE_CREDENTIAL
}
