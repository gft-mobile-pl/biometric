package com.gft.biometric.services

import com.gft.biometric.model.AuthenticationStrength

interface BiometricAuthenticationStatus {
    fun isBiometricAuthenticationSupported(authenticationStrength: AuthenticationStrength): Boolean
    fun areBiometricCredentialsEnrolled(authenticationStrength: AuthenticationStrength): Boolean
    fun isBiometricAuthenticationUsable(authenticationStrength: AuthenticationStrength): Boolean
}
