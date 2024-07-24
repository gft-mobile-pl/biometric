package com.gft.biometric.api.services

import com.gft.biometric.api.model.AuthenticationStrength

interface BiometricAuthenticationStatus {
    fun isBiometricAuthenticationSupported(authenticationStrength: AuthenticationStrength): Boolean
    fun areBiometricCredentialsEnrolled(authenticationStrength: AuthenticationStrength): Boolean
    fun isBiometricAuthenticationUsable(authenticationStrength: AuthenticationStrength): Boolean
}
