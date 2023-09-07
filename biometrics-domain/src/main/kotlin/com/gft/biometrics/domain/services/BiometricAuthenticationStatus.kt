package com.gft.biometrics.domain.services

import com.gft.biometrics.domain.model.AuthenticationStrength

interface BiometricAuthenticationStatus {
    fun isBiometricAuthenticationSupported(authenticationStrength: AuthenticationStrength): Boolean
    fun areBiometricCredentialsEnrolled(authenticationStrength: AuthenticationStrength): Boolean
}

